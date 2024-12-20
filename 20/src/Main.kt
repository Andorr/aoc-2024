import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${solve(input, 2)}")
    println("Part02: ${solve(input, 20)}")
}

fun solve(input: Input, cheatTime: Int): Int {
    val visited = dijkstra(input.m, input.start, input.end)!!

    return visited.keys.sumOf { pos ->
        visited.keys
            .filter { pos.distance(it).let { it in 1..cheatTime } }
            .count { newPos ->
                val newSteps = visited[input.end]!! - visited[newPos]!!
                val newDist = visited[pos]!! + newSteps + pos.distance(newPos)
                val diff = visited[input.end]!! - newDist
                diff >= 100
        }
    }
}

data class Node(val pos: Coord, val steps: Int)

fun dijkstra(grid: Array<CharArray>, start: Coord, end: Coord):  Map<Coord, Int>? {
    val visited = mutableMapOf<Coord, Int>()
    val queue = mutableListOf(Node(start, 0))
    while(queue.isNotEmpty()) {
        val curNode = queue.removeLast()
        visited[curNode.pos] = curNode.steps

        if(curNode.pos == end) {
            return visited
        }

        curNode.pos.neighbours()
            .filter { !visited.contains(it) && (grid[it.y][it.x] != '#') }
            .forEach {
                queue.add(Node(it, curNode.steps + 1))
            }
    }

    return null
}

data class Input(val m: Array<CharArray>, val start: Coord, val end: Coord)
data class Coord(val x: Int, val y: Int) {
    operator fun plus(o: Coord): Coord = Coord(x + o.x, y + o.y)
    operator fun minus(o: Coord): Coord = Coord(x - o.x, y - o.y)
    fun distance(o: Coord): Int = abs(x - o.x) + abs(y - o.y)
    fun neighbours(): List<Coord> = DIRECTIONS.map { this + it }

    companion object {
        val DIRECTIONS = listOf(
            Coord(-1, 0),
            Coord(1, 0),
            Coord(0, 1),
            Coord(0, -1),
        )
    }
}

fun parse(fileName: String): Input {
    var start: Coord? = null
    var end: Coord? = null
    return File(fileName).readLines()
        .mapIndexed { y, it ->
            it.indexOf('S')?.takeIf { it != -1 }?.let { Coord(it, y) }
                ?.also { start = it }
            it.indexOf('E')?.takeIf { it != -1 }?.let { Coord(it, y) }
                ?.also { end = it }
            it.toCharArray()
        }
        .let {
            Input(it.toTypedArray(), start!!, end!!)
        }
}