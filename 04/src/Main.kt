import java.io.File

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Array<String>): Long {
    val allDirections = listOf<Coord>(
        Coord(1, 0),
        Coord(-1, 0),
        Coord(0, 1),
        Coord(0, -1),
        Coord(-1, -1),
        Coord(1, -1),
        Coord(1, 1),
        Coord(-1, 1),
    )
    return input
        .mapIndexed { yIndex, s -> s.withIndex().filter { c -> c.value == 'X' }.map { Coord(it.index, yIndex) } }
        .flatten().sumOf { xIndicies ->
            allDirections.map { dir ->
                val output = getStringByDirection(input, Coord(xIndicies.x, xIndicies.y), dir, 4)
                output
            }.count { it == "XMAS" }.toLong()
        }
}

fun part02(input: Array<String>): Long {
    val dirs = listOf<Coord>(
        Coord(-1, -1),
        Coord(1, -1),
    )
    return input
        .mapIndexed { yIndex, s -> s.withIndex().filter { c -> c.value == 'A' }.map { Coord(it.index, yIndex) }}
        .flatten()
        .count { xIndicies ->
            val center = Coord(xIndicies.x, xIndicies.y)
            val output1 = getStringByDirection(input, Coord(center.x - dirs.first().x, center.y - dirs.first().y), dirs.first(), 3)
            val output2 = getStringByDirection(input, Coord(center.x - dirs.last().x, center.y - dirs.last().y), dirs.last(), 3)

            (output1 == "MAS" || output1 == "SAM") && (output2 == "MAS" || output2 == "SAM")
        }.toLong()
}

fun parse(fileName: String): Array<String> {
    return File(fileName).readLines().toTypedArray()
}

fun getStringByDirection(puzzle: Array<String>, start: Coord, direction: Coord, length: Int = 4): String
{
    val sb = StringBuilder();
    var curPos = start
    while(curPos.x >= 0 && curPos.x < puzzle.first().length && curPos.y >= 0 && curPos.y < puzzle.size && sb.length < length)
    {
        sb.append(puzzle[curPos.y][curPos.x])
        curPos = Coord(curPos.x + direction.x, curPos.y + direction.y)
    }
    return sb.toString()
}

data class Coord(val x: Int, val y: Int)