import java.io.File

fun main() {
    val input = parse("input.txt")

    // println("Part 01: ${part01(input)}")
    println("Part 02: ${part02(input)}")
}

fun part01(input: LongArray): Long {

    return input.map { it.generate(2000).last() }.sum()
}

fun part02(input: LongArray): Long {
    val seqDiffs = mutableMapOf<String, Long>()

    input.forEach {
        val secrets = it.generate(2000).toList()
        val diffs = secrets.zipWithNext().map { it.second % 10 - it.first % 10 }

        val seen = mutableSetOf<String>()

        (0..secrets.lastIndex - 4).forEach { i ->
            val seq = diffs.slice(i..i + 3).joinToString("")
            if (!seen.contains(seq)) {
                seqDiffs[seq] = seqDiffs.getOrDefault(seq, 0L) + secrets[i + 4] % 10
                seen.add(seq)
            }
        }
    }
    return seqDiffs.values.max()
}

fun Long.generate(n: Int): Sequence<Long> = generateSequence(this) { it.next() }.take(n + 1)

fun Long.next(): Long {
    var n = this
    n = (n xor (n shl 6)) % 16777216
    n = (n xor (n shr 5)) % 16777216
    n = (n xor (n * 2048)) % 16777216
    return n
}

fun parse(fileName: String): LongArray {
    return File(fileName).readLines().map { it.toLong() }.toLongArray()
}
