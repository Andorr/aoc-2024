import java.io.File

fun main() {
    val input = parse("input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Array<String>): Long = part0X(input) { m , dx, dy, coord, coord2 ->
    val a = Coord(coord.x - dx , coord.y - dy)
    if(input.isInBounds(a))
    {
        m.add(a)
    }
    val b = Coord(coord2.x + dx , coord2.y + dy)
    if(input.isInBounds(b))
    {
        m.add(b)
    }
}

fun part02(input: Array<String>): Long = part0X(input) { m, dx, dy, antennaA, _ ->
    var a = Coord(antennaA.x - dx, antennaA.y - dy)
    while(input.isInBounds(a))
    {
        m.add(a)
        a = Coord(a.x - dx, a.y - dy)
    }
    var b = Coord(antennaA.x + dx, antennaA.y + dy)
    while(input.isInBounds(b))
    {
        m.add(b)
        b = Coord(b.x + dx, b.y + dy)
    }
}

fun part0X(input: Array<String>, f: (antinodes: MutableSet<Coord>, dy: Int, dx: Int, a: Coord, b: Coord) -> Unit): Long
{
    val antinodes = mutableSetOf<Coord>()
    val groups = input.mapIndexed { y, s ->
        s.indices
            .map { x -> Coord(x, y) }
            .filter { input[it.y][it.x] != '.' }
            .toList()
    }
        .flatten()
        .groupBy { input[it.y][it.x] }

    groups.forEach { (c, coords) ->
        coords.forEach { antennaA ->
            coords.forEach main@{ antennaB ->
                if (antennaB == antennaA) {
                    return@main
                }
                val dy = antennaB.y - antennaA.y
                val dx = antennaB.x - antennaA.x

                f(antinodes, dx, dy, antennaA, antennaB)
            }
        }
    }

    return antinodes.size.toLong()
}

fun Array<String>.isInBounds(coord: Coord): Boolean = coord.x >= 0 && coord.x < this.first().length && coord.y >= 0 && coord.y < this.size

fun parse(fileName: String): Array<String> = File(fileName).readLines().toTypedArray()

data class Coord(val x: Int, val y: Int)