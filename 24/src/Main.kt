import java.io.File

fun main() {
    val input = parse("input.txt")

    println("Part 01: ${part01(input)}")
    println("Part 02: ${part02(input)}")
}

fun part01(input: Input): Long {
    val outputs = mutableMapOf<String, Long>()
    input.wires.forEach { outputs[it.first] = it.second.toLong() }

    val instructions = input.connections.toMutableList()
    while (instructions.isNotEmpty()) {
        val instr =
                instructions.withIndex().first() {
                    outputs.containsKey(it.value.wireA) && outputs.containsKey(it.value.wireB)
                }

        when (instr.value.gate) {
            "AND" ->
                    outputs[instr.value.output] =
                            outputs[instr.value.wireA]!! and outputs[instr.value.wireB]!!
            "OR" ->
                    outputs[instr.value.output] =
                            outputs[instr.value.wireA]!! or outputs[instr.value.wireB]!!
            "XOR" ->
                    outputs[instr.value.output] =
                            outputs[instr.value.wireA]!! xor outputs[instr.value.wireB]!!
        }

        instructions.removeAt(instr.index)
    }

    val finalOutputs = outputs.keys.filter { it.startsWith("z") }.sorted().reversed()
    return finalOutputs.map { outputs[it] }.joinToString("").toLong(2)
}

fun part02(input: Input): String {
    // After analyzing the image from Graphviz, I found the following rules:
    // 1. Outputs of "z"s, except for the last one, should always end with XOR, except for the last
    // one
    // 2. Connections that have outputs of non-"z"s and does not have inputs of X and Y, should
    // always be an AND or OR gate
    // 3. If inputs are X and Y, but not X00 and Y00, and the operator is XOR, the output should be
    // the input of another XOR
    // 4. If inputs are X and Y, but not X00 and Y00, and the operator is AND, the output should be
    // the input of another OR

    return input.connections
            .filter {
                (it.output.startsWith("z") && it.output != "z45" && it.gate != "XOR") ||
                        (!it.output.startsWith("z") &&
                                it.wireA.first() !in "xy" &&
                                it.wireB.first() !in "xy" &&
                                it.gate == "XOR") ||
                        (it.wireA.first() in "xy" &&
                                it.wireB.first() in "xy" &&
                                it.wireA.substring(1) != "00" &&
                                it.wireB.substring(1) != "00" &&
                                it.gate == "XOR" &&
                                !input.connections
                                        .filter { c ->
                                            it.output == c.wireA || it.output == c.wireB
                                        }
                                        .any { c2 -> c2.gate == "XOR" }) ||
                        (it.wireA.first() in "xy" &&
                                it.wireB.first() in "xy" &&
                                it.wireA.substring(1) != "00" &&
                                it.wireB.substring(1) != "00" &&
                                it.gate == "AND" &&
                                !input.connections
                                        .filter { c ->
                                            it.output == c.wireA || it.output == c.wireB
                                        }
                                        .any { c2 -> c2.gate == "OR" })
            }
            .map { it.output }
            .sorted()
            .joinToString(",")
}

fun parse(fileName: String): Input {
    return File(fileName).readLines().let {
        Input(
                it.takeWhile { it.isNotEmpty() }.map {
                    it.split(": ").let { Pair(it.first(), it.last().toInt()) }
                },
                it.takeLastWhile { it.isNotEmpty() }.map {
                    it.split(" ").let { Connection(it[0], it[2], it[1], it[4]) }
                }
        )
    }
}

data class Input(val wires: List<Pair<String, Int>>, val connections: List<Connection>)

data class Connection(val wireA: String, val wireB: String, val gate: String, var output: String)
