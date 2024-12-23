import java.io.File

fun main() {
    val input = parse("input.txt")

    println("Part 01: ${part01(input)}")
    println("Part 02: ${part02(input)}")
}

fun part01(graph: Map<String, List<String>>): Long {

    data class Node(val vertex: String, val distance: Int, val parent: Node?)

    val triplets = mutableSetOf<Triple<String, String, String>>()
    graph.keys.forEach { vertex ->
        val queue = mutableListOf<Node>()
        queue.add(Node(vertex, 0, null))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current.vertex == vertex && current.distance == 3) {
                var triplet =
                        listOf(vertex, current.parent!!.vertex, current.parent.parent!!.vertex)
                                .sorted()
                                .takeIf() { it.any { c -> c.startsWith('t') } }
                                ?.let { Triple(it[0], it[1], it[2]) }
                if (triplet != null) {
                    triplets.add(triplet)
                }
                continue
            }

            if (current.distance >= 3) {
                continue
            }

            graph[current.vertex]?.forEach { neighbor ->
                queue.add(Node(neighbor, current.distance + 1, current))
            }
        }
    }

    return triplets.size.toLong()
}

fun part02(graph: Map<String, List<String>>): String {

    var maxClique: Set<String>? = null
    val visited = mutableSetOf<String>()
    val queue = mutableListOf<Set<String>>()
    queue.addAll(graph.keys.map { setOf(it) })
    while (queue.isNotEmpty()) {
        val clique = queue.removeLast()
        if (maxClique == null || clique.size > maxClique.size) {
            maxClique = clique
        }
        val key = clique.sorted().joinToString()
        if (visited.contains(key)) {
            continue
        }
        visited.add(key)

        val candidates = clique.flatMap { graph[it]!! }.distinct() - clique
        candidates.forEach { candidate ->
            if (clique.all { graph[it]!!.contains(candidate) }) {
                queue.add(clique + candidate)
            }
        }
    }

    return maxClique!!.sorted().joinToString(",")
}

fun parse(fileName: String): Map<String, List<String>> {
    return File(fileName)
            .readLines()
            .map { it.split("-").let { Pair(it.first(), it.last()) } }
            .let { it ->
                buildMap<String, MutableList<String>> {
                    it.forEach { it ->
                        getOrPut(it.first) { mutableListOf() }.add(it.second)
                        getOrPut(it.second) { mutableListOf() }.add(it.first)
                    }
                }
            }
}
