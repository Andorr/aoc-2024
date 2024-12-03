import java.io.File

fun main() {
    val input = parse("input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: List<String>): Long {
    val regex = Regex("mul\\((\\d+),(\\d+)\\)")
    return input
        .map { regex.findAll(it) }
        .flatMap { it }
        .also { println(it) }
        .map {
            val a = it.groupValues[1]!!
            val b = it.groupValues.last()!!
            a.toLong() * b.toLong()
        }
        .sum()
}

fun part02(input: List<String>): Long {
    val regex = Regex("mul\\((\\d+),(\\d+)\\)")
    val regex2 = "do(?:n't)?\\(\\)".toRegex()

    var sum = 0L
    var enabled = true

    input
        .map { regex.findAll(it).toList() }
        .toMutableList()
        .zip(
            input.map { regex2.findAll(it).toList() }
        )
        .forEach {
            it.first.toMutableList().also { l -> l.addAll(it.second) }
                .sortedBy { it.range.first }
                .forEach { item ->
                    if (item.groupValues.first().startsWith("do()")) {
                        enabled = true
                    }
                    else if (item.groupValues.first().startsWith("don't()")) {
                        enabled = false
                    }
                    else if (enabled) {
                        val a = item.groupValues[1]
                        val b = item.groupValues.last()
                        sum += a.toLong() * b.toLong()
                    }
                }
        }

    return sum
}

fun parse(fileName: String): List<String> {
    return File(fileName).readLines()
}