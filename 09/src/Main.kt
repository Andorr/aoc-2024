import java.io.File

fun main() {
    val input = parse("input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(arg: IntArray): Long {
    val input = arg.toMutableList().toIntArray()

    var start = 1
    var end = input.size - 1

    val freeMemory = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()

    while (start < end) {
        val free = input[start]
        val memory = input[end]
        if (free == 0) {
            start += 2
            continue
        }
        if (memory == 0) {
            end -= 2
            continue
        }
        val memoryToMove = if (memory >= free) free else memory
        input[start] -= memoryToMove
        input[end] -= memoryToMove
        val fileId = end / 2

        if (!freeMemory.containsKey(start)) {
            freeMemory[start] = mutableListOf()
        }
        freeMemory[start]?.add(Pair(fileId, memoryToMove))
    }

    var position = 0L
    var sum = 0L
    input.forEachIndexed { index, i ->
        if (index % 2 == 0) {
            sum += (0 ..< i).map { (it.toLong() + position) * (index / 2).toLong() }.sum()
            position += i
        } else {
            if (freeMemory.containsKey(index)) {
                sum +=
                        freeMemory[index]!!
                                .map<Pair<Int, Int>, Long> {
                                    val s =
                                            (0 ..< it.second).sumOf { j ->
                                                (j.toLong() + position) * it.first
                                            }
                                    position += it.second
                                    return@map s
                                }
                                .sum()
            } else {
                return@forEachIndexed
            }
        }
    }

    return sum
}

fun part02(arg: IntArray): Long {
    val input = arg.toList().toIntArray()

    var end = input.size - 1

    val freeMemory = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()

    while (end >= 0) {
        val memory = input[end]
        var start = 1
        lit@ while (start < end) {
            val free = input[start]
            if (free >= memory) {
                input[start] -= memory
                input[end] -= memory
                if (!freeMemory.containsKey(start)) {
                    freeMemory[start] = mutableListOf()
                }
                val fileId = end / 2
                freeMemory[start]?.add(Pair(fileId, memory))
                break@lit
            }
            start += 2
        }

        end -= 2
    }

    var position = 0L
    var sum = 0L
    input.forEachIndexed { index, i ->
        if (index % 2 == 0) {
            if (i == 0) {
                position += arg[index]
                return@forEachIndexed
            }

            sum += (0 ..< i).map { (it.toLong() + position) * (index / 2).toLong() }.sum()
            position += i
        } else {
            if (freeMemory.containsKey(index)) {
                sum +=
                        freeMemory[index]!!
                                .map<Pair<Int, Int>, Long> {
                                    val s =
                                            (0 ..< it.second)
                                                    .sumOf { j ->
                                                        (j.toLong() + position) * it.first
                                                    }
                                                    .toLong()
                                    position += it.second
                                    return@map s
                                }
                                .sum()
                position += input[index]
            } else {
                position += arg[index]
            }
        }
    }
    return sum
}

fun parse(fileName: String): IntArray {
    return File(fileName).readLines().first().map { it.toString().toInt() }.toIntArray()
}
