import java.io.File
import kotlin.math.min

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input.map { Transform(it.pos, it.vel) }, Vector(101, 103))}")
    println("Part02: ${part02(input, Vector(101, 103))}")
}

fun part01(input: List<Transform>, size: Vector, steps: Int = 100): Long {
    var floor = mutableMapOf<Vector, MutableList<Transform>>()

    input.forEach { floor.add(it) }

    var i = 0
    while(i < steps) {

        input.forEach {
            val newPos = it.move(size)
            floor.remove(it)
            it.pos = newPos
            floor.add(it)
        }
        i++
    }

    return floor.values.flatten()
    .filter { !(it.pos.x == size.x/2 || it.pos.y == size.y/2) }
    .groupBy {

        Vector(min(1, it.pos.x/(size.x/2)), min(1, it.pos.y/(size.y/2)))
    }
        .mapValues { it.value.map { it.pos } }
        .map { it.value.size }
        .fold(1L) { acc, i -> acc*i.toLong() }
        .toLong()
}

fun part02(input: List<Transform>, size: Vector): Long {
    var floor = mutableMapOf<Vector, MutableList<Transform>>()

    input.forEach { floor.add(it) }

    var i = 0
    while(true) {

        input.forEach {
            val newPos = it.move(size)
            floor.remove(it)
            it.pos = newPos
            floor.add(it)
        }
        i++
        if(isChristmasTree(floor, size)) {
            printMap(floor, size)
            return i.toLong()

        }
    }
}

fun isChristmasTree(floor: Map<Vector, MutableList<Transform>>, size: Vector): Boolean {

    fun hasRow(start: Vector, size: Int = 0): Boolean {
        return (0..<size).map { Vector(start.x + it, start.y) }
            .all { floor.containsKey(it) }
    }

    for(y in 0..<size.y)
    {
        for(x in 0..<size.x)
        {
            if(hasRow(Vector(x, y), 10))
            {
                return true
            }
        }
    }
    return false
}

fun printMap(floor: MutableMap<Vector, MutableList<Transform>>, size: Vector) {
    for(y in 0..<size.y) {
        for(x in 0..<size.x) {
            val v = Vector(x, y)
            if(floor.contains(v))
            {
                print("#")
            }
            else
            {
                print(".")
            }
        }
        println()
    }
}

fun parse(fileName: String): List<Transform> {
   return File(fileName).readLines()
        .map {
            Regex("-?\\d+").findAll(it)
                .let { it.map { it.value.toInt() }.toList() }
                .let { values ->
                    Transform(
                        Vector(values[0], values[1]),
                        Vector(values[2], values[3])
                    )
                }
        }
        .toList()
}

fun MutableMap<Vector, MutableList<Transform>>.add(transform: Transform) {
    if(!this.containsKey(transform.pos)) {
        this[transform.pos] = mutableListOf()
    }
    this[transform.pos]!!.add(transform)
}

fun MutableMap<Vector, MutableList<Transform>>.remove(transform: Transform) {
    if(!this.containsKey(transform.pos)) {
        return
    }
    this[transform.pos]!!.remove(transform)
    if(this[transform.pos]!!.size == 0)
    {
        this.remove(transform.pos)
    }
}

data class Transform(var pos: Vector, val vel: Vector)
{
    fun move(floorSize: Vector): Vector {
        return Vector(move(pos.x,  vel.x, floorSize.x), move(pos.y, vel.y,floorSize.y))
    }

    private fun move(pos: Int, vel: Int, size: Int): Int {
        return ((pos + vel)%size).let {
            if (it < 0) it + size else it
        }
    }
}
data class Vector(val x: Int, val y: Int)
{

}