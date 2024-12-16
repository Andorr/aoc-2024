import java.io.File
import java.util.PriorityQueue

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Input): Long {
    data class Node(val pos: Coord, val score: Long, val dir: Coord, val steps: Long)

    val visited = mutableSetOf<Pair<Coord, Coord>>()

    val queue = PriorityQueue<Node>(Comparator { t, t2 ->
        t.score.compareTo(t2.score)
    })
    queue.add(Node(input.start, 0L, EAST, 0L))

    while(queue.isNotEmpty()) {
        val (curPos, score, curDir, steps) = queue.poll()
        if(curPos == input.end) {
            return score
        }
        visited.add(Pair(curPos, curDir))


        listOf(
            curDir,
            Coord(-curDir.y, curDir.x),
            Coord(curDir.y, -curDir.x)
        )
            .forEach {
                if(it == curDir) {
                    val newPos = curPos + it
                    if(input.map[newPos.y][newPos.x] != '#' && !visited.contains(Pair(newPos, curDir))) {
                        queue.add(Node(newPos, score + 1, it, steps + 1))
                    }
                } else {
                    if(!visited.contains(Pair(curPos, it))) {
                        queue.add(Node(curPos, score + 1000, it, steps))
                    }
                }
            }
    }

    return -1L
}

fun part02(input: Input): Long {
    data class Node(val pos: Coord, val score: Long, val dir: Coord, val steps: Long, val parent: Node?)

    val visited = mutableSetOf<Pair<Coord, Coord>>()

    val queue = PriorityQueue<Node>(Comparator { t, t2 ->
        t.score.compareTo(t2.score)
    })
    queue.add(Node(input.start, 0L, EAST, 0L, null))

    var bestScore: Long? = null
    val bestTiles = mutableSetOf<Coord>()

    while(queue.isNotEmpty()) {
        val curNode = queue.poll()
        val (curPos, score, curDir, steps) = curNode
        if(curPos == input.end) {
            if(bestScore != null && score > bestScore) {
                return bestTiles.size.toLong()
            }

            bestScore = score
            val tiles = mutableListOf(curPos)
            var n = curNode
            while(n != null) {
                tiles.add(n.pos)
                n = n.parent
            }

            bestTiles.addAll(tiles)
        }
        visited.add(Pair(curPos, curDir))


        listOf(
            curDir,
            Coord(-curDir.y, curDir.x),
            Coord(curDir.y, -curDir.x)
        )
            .forEach {
                if(it == curDir) {
                    val newPos = curPos + it
                    if(input.map[newPos.y][newPos.x] != '#' && !visited.contains(Pair(newPos, curDir))) {
                        queue.add(Node(newPos, score + 1, it, steps + 1, curNode))
                    }
                } else {
                    if(!visited.contains(Pair(curPos, it))) {
                        queue.add(Node(curPos, score + 1000, it, steps, curNode))
                    }
                }
            }
    }

    return -1L
}

val EAST = Coord(1, 0)
val WEST = Coord(-1, 0)
val NORTH = Coord(0, -1)
val SOUTH = Coord(0, 1)

val directions = mutableListOf(
    EAST, WEST, NORTH, SOUTH
)

fun parse(fileName: String): Input {
    return File(fileName).readLines()
        .let { lines ->
            Input(
                lines.map { it.toCharArray() }.toTypedArray(),
                lines.mapIndexed { i, it -> Pair(i, it.indexOf('S')) }.first { it.second != -1 }.let { it ->
                    Coord(it.second, it.first)
                },
                lines.mapIndexed { i, it -> Pair(i, it.indexOf('E')) }.first { it.second != -1 }.let { it ->
                    Coord(it.second, it.first)
                }
            )
        }
}

data class Input(val map: Array<CharArray>, val start: Coord, val end: Coord)

data class Coord(val x: Int, val y: Int) {
    operator fun plus(o: Coord): Coord = Coord(this.x + o.x, this.y + o.y)
    operator fun times(o: Coord): Coord = Coord(this.x * o.x, this.y * o.y)
}