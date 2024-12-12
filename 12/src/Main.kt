import java.io.File

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Array<CharArray>): Long {

    val visited = mutableSetOf<Coord>()
    var price = 0L

    for(y in 0..<input.size) {
        for(x in 0..<input.first().size) {
            val s = Coord(x, y)
            if(visited.contains(s)) {
                continue
            }
            val result = input.dfs(s, visited)
            price += result.area*result.perimeter
        }
    }

    return price
}

fun part02(input: Array<CharArray>): Long {

    val visited = mutableSetOf<Coord>()
    var price = 0L

    for(y in 0..<input.size) {
        for(x in 0..<input.first().size) {
            val s = Coord(x, y)
            if(visited.contains(s)) {
                continue
            }
            val result = input.dfs2(s, visited)
            price += result.area*result.perimeter
        }
    }

    return price
}

fun Array<CharArray>.dfs2(start: Coord, visited: MutableSet<Coord>): Result {

    val target = this[start.y][start.x]
    val queue = mutableListOf<Coord>()
    queue.add(start)

    val perimeters = mutableSetOf<Pair<Coord, Coord>>()

    var area = 0L

    while(queue.isNotEmpty())
    {
        val pos = queue.removeLast()
        if(visited.contains(pos)) {
            continue
        }

        visited.add(pos)
        area++

        // Neighbours
        val directions = listOf(Coord(1, 0), Coord(-1, 0), Coord(0, 1), Coord(0, -1))
        val neighbours = directions.map { dir -> pos + dir }
        neighbours.filter { this.isInBounds(it.y, it.x) && this[it.y][it.x] == target && !visited.contains(it) }
            .forEach {
                queue.add(it)
            }

        directions.filter { dir ->
            val newPos = dir + pos
            !this.isInBounds(newPos.y, newPos.x) || this[newPos.y][newPos.x] != target
        }.forEach { dir ->
            val newPos = dir + pos
            perimeters.add(Pair(newPos, dir))
        }
    }

    var sides = 0L

    val queue2 = perimeters.toMutableList()
    while(queue2.isNotEmpty())
    {
        val (pos, dir) = queue2.last()
        sides++

        val local = mutableListOf<Coord>()
        local.add(pos)
        while(local.isNotEmpty()) {
            val localPos = local.removeLast()
            if(!queue2.contains(Pair(localPos, dir))) {
                continue
            }
            queue2.remove(Pair(localPos, dir))

            val directions = listOf(Coord(1, 0), Coord(-1, 0), Coord(0, 1), Coord(0, -1))
            val neighbours = directions.map { localDir -> localPos + localDir }.filter { queue2.contains(Pair(it, dir)) }
            local.addAll(neighbours)
        }

    }

    return Result(area, sides, target)
}

fun Array<CharArray>.dfs(start: Coord, visited: MutableSet<Coord>): Result {

    val target = this[start.y][start.x]
    val queue = mutableListOf<Coord>()
    queue.add(start)

    var area = 0L
    var perimeter = 0L

    while(queue.isNotEmpty())
    {
        val pos = queue.removeLast()
        if(visited.contains(pos))
        {
            continue
        }

        visited.add(pos)
        area++

        // Neighbours
        val directions = listOf(Coord(1, 0), Coord(-1, 0), Coord(0, 1), Coord(0, -1))
        val neighbours = directions.map { dir -> pos + dir }
        neighbours.filter { this.isInBounds(it.y, it.x) && this[it.y][it.x] == target && !visited.contains(it) }
            .forEach {
                queue.add(it)
            }
        perimeter += neighbours.count { !this.isInBounds(it.y, it.x) || this[it.y][it.x] != target }
    }

    return Result(area, perimeter, target)
}

fun Array<CharArray>.isInBounds(y: Int, x: Int): Boolean = x >= 0 && x < this.first().size && y >=0 && y < this.size

fun parse(fileName: String): Array<CharArray> {
    return File(fileName).readLines().map { it.toCharArray() }.toTypedArray()
}

data class Result(val area: Long, val perimeter: Long, val target: Char)
data class Coord(val x: Int, val y: Int) {
    operator fun plus(o: Coord): Coord = Coord(x + o.x, y + o.y)
}