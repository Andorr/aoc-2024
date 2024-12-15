import java.io.File


fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input.copy())}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Input): Long {

    var curPos = input.start
    input.path.forEach { step ->

        val dir = directions[step]!!

        val boxes = mutableListOf<Coord>()
        var nextStep = curPos + dir
        while (input.map[nextStep.y][nextStep.x] == 'O')
        {
            boxes.add(nextStep)
            nextStep += dir
        }

        if(input.map[nextStep.y][nextStep.x] == '#')
        {
            return@forEach
        }

        if(boxes.isNotEmpty()) {
            val firstBox = boxes.first()
            input.map[firstBox.y][firstBox.x] = '.'
            input.map[nextStep.y][nextStep.x] = 'O'

        }

        curPos += dir
    }

    return input.map.mapIndexed { y, chars ->
        chars.mapIndexed { x, c ->
            if(c != 'O') 0 else y*100L + x
        }
            .sum()
    }.sum()
}

fun part02(arg: Input): Long {
    val input = arg.expand()

    var curPos = input.start
    input.path.forEach { step ->
        // printMap(input.map, curPos)
        val dir = directions[step]!!

        val boxes = mutableListOf<Pair<Coord, Coord>>()
        var nextSteps = mutableListOf(
            curPos + dir
        )
        while (nextSteps.any { nextStep -> input.map[nextStep.y][nextStep.x].let { "[]".contains(it)}} &&
            nextSteps.all { nextStep -> input.map[nextStep.y][nextStep.x] != '#' })
        {
            if(dir.x != 0) {
                boxes.add(nextSteps.first().let { Pair(it, it + dir) })
                nextSteps = mutableListOf(nextSteps.first().let { Coord(it.x + dir.x*2, it.y) })
            } else {
                val newBoxes = nextSteps.filter {
                    input.map[it.y][it.x].let { "[]".contains(it)}
                }
                .map { step ->
                    when(input.map[step.y][step.x]) {
                        '[' -> Pair(step, step + Coord(1, 0))
                        ']' -> Pair(step, step + Coord(-1, 0))
                        else -> Pair(step, step) // Not possible
                    }
                }
                boxes.addAll(newBoxes)
                nextSteps = newBoxes.map { listOf(it.first + dir, it.second + dir) }.flatten().toMutableList()
            }
        }

        if(nextSteps.any { nextStep -> input.map[nextStep.y][nextStep.x] == '#' })
        {
            return@forEach
        }

        if(boxes.isNotEmpty()) {
            boxes.forEach {
                input.map[it.first.y][it.first.x] = '.'
                input.map[it.second.y][it.second.x] = '.'
            }
            if(dir.x != 0) {

                boxes.map {
                    Pair(it.first + dir, it.second + dir)
                }.forEach {
                    input.map[it.first.y][it.first.x] = if(dir.x > 0) '[' else ']'
                    input.map[it.second.y][it.second.x] = if(dir.x > 0) ']' else '['
                }
            } else {
                boxes.map {
                    Pair(it.first + dir, it.second + dir)
                }.forEach {
                    input.map[it.first.y][it.first.x] = if(it.first.x > it.second.x) ']' else '['
                    input.map[it.second.y][it.second.x] = if(it.first.x > it.second.x) '[' else ']'
                }
            }
        }

        curPos += dir
    }
    // printMap(input.map, curPos)

    return input.map.mapIndexed { y, chars ->
        chars.mapIndexed { x, c ->
            if(!"[".contains(c)) 0 else y*100L + x
        }
            .sum()
    }.sum()
}

val directions = mutableMapOf<Char, Coord>(
    '>' to Coord(1, 0),
    '<' to Coord(-1, 0),
    '^' to Coord(0, -1),
    'v' to Coord(0, 1)
)

fun parse(fileName: String): Input {
    var lines = File(fileName).readLines()

    val map = lines.takeWhile { it.isNotBlank() }.map { it.toCharArray() }.toTypedArray()
    val path = lines.takeLastWhile { it.isNotBlank() }.joinToString("") { it }.toCharArray()
    val start = map.indices.map { y -> map[y].indices.map { Pair(y, it) } }.flatten().find { map[it.first][it.second] == '@' }!!.let { Coord(it.second, it.first) }
    map[start.y][start.x] = '.'
    return Input(map, path, start)
}

data class Input(val map: Array<CharArray>, val path: CharArray, val start: Coord)
{
    fun copy(): Input {
        return Input(
            map.map { it.copyOf() }.toTypedArray(),
            path,
            start
        )
    }

    fun expand(): Input {
        val newMap = map.map {
            it.map { c ->
                when(c) {
                    'O' -> "[]"
                    '.' -> ".."
                    '#' -> "##"
                    else -> ".."
                }
            }.joinToString("") { it }.toCharArray()
        }.toTypedArray()

        return Input(newMap, path, Coord(start.x*2, start.y))
    }
}

data class Coord(val x: Int, val y: Int)
{
    operator fun plus(o: Coord): Coord = Coord(this.x + o.x, this.y + o.y)
    operator fun times(i: Int): Coord = Coord(this.x*i, this.y*i)
}