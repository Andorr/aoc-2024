import java.io.File

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")
    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Array<String>): Long {
    val allDirections = listOf(
        Coord(1, 0), Coord(-1, 0),
        Coord(0, 1), Coord(0, -1),
        Coord(-1, -1), Coord(1, -1),
        Coord(1, 1), Coord(-1, 1),
    )
    return calcOnTargetLetters(input, 'X') {
        it.sumOf { xIndicies ->
            allDirections.map { dir -> wordInDirection(input, Coord(xIndicies.x, xIndicies.y), dir, 4) }
            .count { word -> word == "XMAS" }
            .toLong()
        }
    }
}

fun part02(input: Array<String>): Long {
    val topLeftCorner = Coord(-1, -1)
    val topRightCorner = Coord(1, -1)
    return calcOnTargetLetters(input, 'A') {
        it.count { xIndicies ->
            val center = Coord(xIndicies.x, xIndicies.y)
            val word1 = wordInDirection(input, center - topLeftCorner, topLeftCorner, 3)
            val word2 = wordInDirection(input, center - topRightCorner, topRightCorner, 3)
            (word1 == "MAS" || word1 == "SAM") && (word2 == "MAS" || word2 == "SAM")
        }.toLong()
    }
}

fun calcOnTargetLetters(puzzle: Array<String>, targetChar: Char, f: (List<Coord>) -> Long): Long =
    puzzle
        .mapIndexed { yIndex, s -> s.withIndex().filter { char -> char.value == targetChar }.map { Coord(it.index, yIndex) }}
        .flatten()
        .let { f(it) }

fun wordInDirection(puzzle: Array<String>, start: Coord, direction: Coord, length: Int): String
{
    val sb = StringBuilder();
    var curPos = start
    while(curPos.x >= 0 && curPos.x < puzzle.first().length && curPos.y >= 0 && curPos.y < puzzle.size && sb.length < length)
    {
        sb.append(puzzle[curPos.y][curPos.x])
        curPos += direction
    }
    return sb.toString()
}

fun parse(fileName: String): Array<String> = File(fileName).readLines().toTypedArray()

data class Coord(val x: Int, val y: Int)
{
    operator fun plus(other: Coord) = Coord(this.x + other.x, this.y + other.y)
    operator fun minus(other: Coord) = Coord(this.x - other.x, this.y - other.y)
}

