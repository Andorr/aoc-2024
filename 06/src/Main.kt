import java.io.File

fun main() {
    val input = parse("input.txt")

//    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Array<String>): Long {


    val m = mutableSetOf<Coord>()

    val directions = listOf(
        Coord(0, -1), Coord(1, 0), Coord(0, 1), Coord(-1, 0)
    )
    var curDirection = 0
    var curPos = Coord(0, 0)
    for(y in 0..<input.size)
    {
        for(x in 0..<input.first().length) {
            if (input[y][x] == '^') {
                curPos = Coord(x, y)
                break;
            }
        }
    }

    fun isInBounds(pos: Coord) = pos.x >= 0 && pos.x < input.first().length && pos.y >= 0 && pos.y < input.size

    while(isInBounds(curPos))
    {
        m.add(curPos)
        var nextPos = curPos + directions[curDirection]
        if(isInBounds(nextPos) && input[nextPos.y][nextPos.x] == '#')
        {
            curDirection = (curDirection+1)%directions.size
        }
        curPos += directions[curDirection]
    }

    return m.size.toLong()
}

fun part02(input: Array<String>): Long {
    var count = 0L
    for(y in input.indices)
    {
        for(x in input.first().indices)
        {
            if(canLoop(input, Coord(x, y)))
            {
                count++
            }
        }
    }
    return count
}

fun canLoop(input: Array<String>, obstacle: Coord): Boolean
{
    val visited = mutableSetOf<String>()
    val directions = listOf(
        Coord(0, -1), Coord(1, 0), Coord(0, 1), Coord(-1, 0)
    )
    var curDirection = 0
    var curPos = Coord(0, 0)
    for(y in input.indices)
    {
        for(x in input.first().indices) {
            if (input[y][x] == '^') {
                curPos = Coord(x, y)
                break;
            }
        }
    }

    if(obstacle == curPos || input[obstacle.y][obstacle.x] == '#')
    {
        return false
    }

    fun isInBounds(pos: Coord) = pos.x >= 0 && pos.x < input.first().length && pos.y >= 0 && pos.y < input.size

    while(isInBounds(curPos))
    {


        val nextPos = curPos + directions[curDirection]
        if(isInBounds(nextPos) && (input[nextPos.y][nextPos.x] == '#' || (nextPos.y == obstacle.y && nextPos.x == obstacle.x)) )
        {
            val state = "$curPos.x,$curPos.y,$curDirection"
            if(visited.contains(state))
            {
                return true
            }
            visited.add(state)
//            val visitedCount = visited.size
//            visited.add("$curPos.x,$curPos.y,$curDirection")
//            if(visitedCount == visited.size)
//            {
//                return true
//            }
            curDirection = (curDirection+1)%directions.size
            continue
        }
        curPos += directions[curDirection]
    }

    return false
}



fun parse(fileName: String): Array<String> {
    return File(fileName)
        .readLines()
        .toTypedArray()
}

data class Coord(val x: Int, val y: Int)
{
    operator fun plus(o: Coord) = Coord(this.x + o.x, this.y + o.y)
    operator fun minus(o: Coord) = Coord(this.x - o.x, this.y - o.y)
}