import java.io.File

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Array<IntArray>): Long = input.indices.sumOf { y ->
    input[y].indices.sumOf { x ->
        if(input[y][x] == 0) input.hike(Coord(x, y), OutputType.Score) else 0L }}

fun part02(input: Array<IntArray>): Long = input.indices.sumOf { y ->
        input[y].indices.sumOf { x ->
            if(input[y][x] == 0) input.hike(Coord(x, y), OutputType.Rating) else 0L }}


fun Array<IntArray>.hike(start: Coord, output: OutputType): Long {

    val uniqueTops = mutableSetOf<Coord>()
    var rating = 0L
    val neighbourDirs = listOf<Coord>(
        Coord(-1, 0),
        Coord(1, 0),
        Coord(0, 1),
        Coord(0, -1),
    )

    val queue = mutableListOf<Coord>()
    queue.add(start)
    while(queue.isNotEmpty()) {
        val pos = queue.removeLast()
        val height = this[pos.y][pos.x]

        if(height == 9)
        {
            uniqueTops.add(pos)
            rating++
            continue
        }

        val n = neighbourDirs.map{pos + it}.filter { this.isInBounds(it) && this[it.y][it.x] == height + 1 }
        queue.addAll(
            n
        )
    }

    return if(output == OutputType.Rating) rating else uniqueTops.size.toLong()
}

fun Array<IntArray>.isInBounds(pos: Coord): Boolean = pos.x >= 0 && pos.x < this.first().size && pos.y >= 0 && pos.y < this.size;

fun parse(fileName: String): Array<IntArray> {
    return File(fileName).readLines()
        .map { it.map { c -> c.digitToInt() }.toIntArray() }
        .toTypedArray()
}

enum class OutputType {
    Score,
    Rating
}

data class Coord(val x: Int, val y: Int) {
    operator fun minus(o: Coord): Coord = Coord(this.x - o.x, this.y - o.y)
    operator fun plus(o: Coord): Coord = Coord(this.x + o.x, this.y + o.y)
}