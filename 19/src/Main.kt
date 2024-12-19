import java.io.File
import kotlin.math.min

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Input): Long {

    val maxL = input.patterns.maxOf { it.length }
    val cache = mutableMapOf<String, Boolean>()

    fun hasAnySolution(s: String): Boolean {
        if(s.isEmpty()) {
            return true
        }

        val maxLength = min(maxL, s.length)
        return (maxLength - 1 downTo 0).filter {
            input.patterns.contains(s.slice(0..it))
        }.any {
            val newS = s.drop(it+1)
            if(cache.containsKey(newS)) {
                return@any cache[newS]!!
            }
            val result = hasAnySolution(s.drop(it+1))
            cache[newS] = result
            result
        }

    }

    return input.designs.count {
        hasAnySolution(it)
    }.toLong()
}

fun part02(input: Input): Long {

    val maxL = input.patterns.maxOf { it.length }
    val cache = mutableMapOf<String, Long>()

    fun countCombinations(s: String): Long {
        if(s.isEmpty()) {
            return 1
        }

        val maxLength = min(maxL, s.length)
        return (maxLength - 1 downTo 0).filter {
            input.patterns.contains(s.slice(0..it))
        }.sumOf {
            val newS = s.drop(it+1)
            if(cache.containsKey(newS)) {
                return@sumOf cache[newS]!!
            }
            val result = countCombinations(s.drop(it+1))
            cache[newS] = result
            result
        }

    }

    return input.designs.sumOf {
        countCombinations(it)
    }
}

fun parse(fileName: String): Input {
    return File(fileName).readLines().let { lines ->
        Input(
            lines.first().split(", ").toSet(),
            lines.drop(2).toTypedArray()
        )
    }
}

data class Input(val patterns: Set<String>, val designs: Array<String>)