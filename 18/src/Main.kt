import java.io.File
import java.util.PriorityQueue

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input, Coord(71, 71), 1024)}")
    println("Part02: ${part02(input, Coord(71, 71), 1024)}")
}

fun part01(input: Array<Coord>, size: Coord, byteSize: Int): Long {

    val start = Coord(0, 0)
    val end = Coord(size.x-1, size.y-1)

    val m = mutableSetOf<Coord>()
    input.take(byteSize).forEach { m.add(it) }

    data class Node(val pos: Coord, val step: Long)
    val visited = mutableSetOf<Coord>()
    val queue = PriorityQueue<Node>({a, b -> a.step.compareTo(b.step)})
    queue.add(Node(start, 0L))
    while(queue.isNotEmpty()) {
        val node = queue.poll()
        if(visited.contains(node.pos)) {
            continue
        }
        visited.add(node.pos)

        if(node.pos == end) {
            return node.step
        }

        node.pos.neighbours()
            .filter { !visited.contains(it) && !m.contains(it) && size.isInBounds(it) }
            .forEach { neighbour ->
                queue.add(Node(neighbour, node.step + 1))
            }
    }
    return -1L
}

fun part02(input: Array<Coord>, size: Coord, byteSize: Int): String {

    val start = Coord(0, 0)
    val end = Coord(size.x-1, size.y-1)

    val m = mutableSetOf<Coord>()
    input.take(byteSize).forEach { m.add(it) }

    fun foundPath(): Boolean {
        data class Node(val pos: Coord, val step: Long)
        val visited = mutableSetOf<Coord>()
        val queue = PriorityQueue<Node>({a, b -> a.step.compareTo(b.step)})
        queue.add(Node(start, 0L))
        while(queue.isNotEmpty()) {
            val node = queue.poll()
            if(visited.contains(node.pos)) {
                continue
            }
            visited.add(node.pos)

            if(node.pos == end) {
                return true
            }

            node.pos.neighbours()
                .filter { !visited.contains(it) && !m.contains(it) && size.isInBounds(it) }
                .forEach { neighbour ->
                    queue.add(Node(neighbour, node.step + 1))
                }
        }
        return false
    }

    val s = input.drop(byteSize).first {
        m.add(it)
        !foundPath()
    }
    return "${s.y},${s.x}"
}


fun parse(fileName: String): Array<Coord> {
    return File(fileName).readLines()
        .map { it.split(",") }
        .map { Coord(it.last().toInt(), it.first().toInt()) }
        .toTypedArray()
}


data class Coord(val x: Int, val y: Int) {
    operator fun plus(o: Coord): Coord = Coord(this.x + o.x, this.y + o.y)
    operator fun minus(o: Coord): Coord = Coord(this.x - o.x, this.y - o.y)

    fun neighbours(): List<Coord> = DIRECTIONS.map { this + it }

    fun isInBounds(o: Coord): Boolean = o.x >= 0 && o.x < this.x && o.y >= 0 && o.y < this.y

    companion object {
        val DIRECTIONS = mutableListOf(
            Coord(-1, 0),
            Coord(1, 0),
            Coord(0, 1),
            Coord(0, -1)
        )
    }
}