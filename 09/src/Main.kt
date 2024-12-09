import java.io.File
import kotlin.math.max

fun main() {
    val input = parse("input.txt")

    println("Part01: ${part01(input)}")
}

fun part01(input: IntArray): Long {

    var start = 1
    var end = input.size - 1

    val freeMemory = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()

    while(start < end)
    {
        val free = input[start]
        val memory = input[end]
        if(free == 0)
        {
            start += 2
            continue
        }
        if(memory == 0)
        {
            end -= 2
        }
        val memoryToMove = if (memory > free) free else memory
        input[start] -= memoryToMove
        input[end] -= memoryToMove
        val fileId = end/2

        if (!freeMemory.containsKey(start))
        {
            freeMemory[start] = mutableListOf()
        }
        freeMemory[start]?.add(Pair(fileId, memoryToMove))
    }
    println(input.toList().slice(0..end+3))


    println(input.toList())

    val s = input.mapIndexed { index, i ->
        if(index%2 == 0) {
            return@mapIndexed (0..<i).map { index/2 }.joinToString("")
        }
        else
        {
            return@mapIndexed freeMemory[index]?.map {p -> (0..<p.second).map { p.first }.joinToString("") }?.joinToString("")
        }
    }.joinToString("")
    println(s)
    return s.takeWhile { it.isDigit() }.mapIndexed { index, c -> index * c.digitToInt().toLong() }.sum()
}

fun parse(fileName: String): IntArray {
    return File(fileName).readLines().first().map { it.toString().toInt() }.toIntArray()
}