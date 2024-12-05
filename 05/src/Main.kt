import java.io.File

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part01: ${part02(input)}")
}

fun part01(input: Input): Long {
    return input.updates.filter { isInOrder(it, input) }.sumOf { it[it.size / 2].toLong() }
}

fun part02(input: Input): Long {
    return input.updates
        .filter { !isInOrder(it, input) }
        .map { update ->
            val orders = input.order.filter { (update.contains(it.first) && update.contains(it.second)) }
            topologicalSort(update, orders.toMutableList())
        }.sumOf { it[it.size / 2].toLong() }
}

fun isInOrder(update: List<Int>, input: Input): Boolean =
    update.indices.toList()
        .slice(0..<update.size-1)
        .all { i -> input.order
            .filter { it.first == update[i] }
            .map { it.second }.contains(update[i+1])
        }


fun topologicalSort(update: List<Int>, orders: MutableList<Pair<Int, Int>>): List<Int>
{
    val edgeTargets = orders.map { it.second }
    val startNode = update.first { !edgeTargets.contains(it) }

    val sorted = mutableListOf<Int>()
    val nodes = mutableListOf(startNode)

    while(nodes.isNotEmpty())
    {
        val curNode = nodes.removeLast()
        sorted.add(curNode)

        val edgesFromCurNode = orders.filter { it.first == curNode }
        orders.removeAll(edgesFromCurNode)

        edgesFromCurNode.map { it.second }
            .filter { neighbour -> !orders.any { it.second == neighbour } }
            .forEach { nodes.add(it) }
    }
    return sorted
}

fun parse(fileName: String): Input {
    val lines = File(fileName).readLines()
    val a = lines.takeWhile { it.contains("|") }.map { it.split("|").let { it.first().toInt() to it.last().toInt() } }
    val b = lines.takeLastWhile { it.contains(",") }.map { it.split(",").map { it.toInt() } }
    return Input(a, b)
}

data class Input(val order: List<Pair<Int, Int>>, val updates: List<List<Int>>)