import java.io.File

fun main() {
    val input = parse("input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Array<String>): Long {
    val steps = mutableSetOf<Coord>()
    startGuard(input, input.startPos()) {
        steps.add(it)
    }
    return steps.size.toLong()
}

fun part02(input: Array<String>): Long {
    val steps = mutableSetOf<Coord>()
    startGuard(input, input.startPos()) {
        steps.add(it)
    }
    return steps.count { obstacle -> startGuard(input, input.startPos(), obstacle) }.toLong()
}

fun startGuard(input: Array<String>, startPos: Coord, obstacle: Coord? = null, onStep: ((Coord) -> Unit)? = null): Boolean
{
    val visited = mutableSetOf<Pair<Coord, Int>>()
    val directions = listOf(
        Coord(0, -1), Coord(1, 0), Coord(0, 1), Coord(-1, 0)
    )
    var curDirection = 0
    var curPos = startPos

    if(obstacle == curPos)
    {
        return false
    }

    fun isInBounds(pos: Coord) = pos.x >= 0 && pos.x < input.first().length && pos.y >= 0 && pos.y < input.size

    while(isInBounds(curPos))
    {
        onStep?.let { it(curPos) }
        val nextPos = curPos + directions[curDirection]
        if(isInBounds(nextPos) && (input[nextPos.y][nextPos.x] == '#' || nextPos == obstacle))
        {
            val state = Pair(curPos, curDirection)
            if(visited.contains(state))
            {
                return true
            }
            visited.add(state)
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

fun Array<String>.startPos(): Coord = this.withIndex()
    .firstNotNullOf { (y, row) ->
        row.indexOf('^').takeIf { it != -1 }?.let { x -> Coord(x, y) }
    }

data class Coord(val x: Int, val y: Int)
{
    operator fun plus(o: Coord) = Coord(this.x + o.x, this.y + o.y)
}