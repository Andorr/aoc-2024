import java.io.File

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part01: ${part02(input)}")
}

fun part01(input: LongArray): Long = run(input, 25,0L, 0,  mutableMapOf())
fun part02(input: LongArray): Long = run(input, 75,0L, 0,  mutableMapOf())

fun run(input: LongArray, depthLimit: Int, cnt: Long = 0, depth: Int = 0, d: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()): Long {

    if(depth == depthLimit)
    {
        return input.size.toLong()
    }

   return input.sumOf { it ->
       val state = Pair(it, depth)
       if(d.containsKey(state)) {
           return@sumOf d.get(state)!! + cnt
       }

       val newCount: Long = run(it.blink(), depthLimit, cnt, depth + 1, d)

       d[state] = newCount


      return@sumOf cnt + newCount
   }
}


fun Long.blink(): LongArray {
    return when (this) {
        0L -> longArrayOf(1L)
        else -> {
            val s = this.toString()
            if (s.length%2 == 0) {
               return longArrayOf(
                   s.slice(0..<s.length/2).toLong(),
                   s.slice(s.length/2..<s.length).toLong()
               )
            }
            else {
                return longArrayOf(this*2024L)
            }
        }
    }

}

fun parse(fileName: String): LongArray {
    return File(fileName).readLines().first().split(" ").map { it.toLong() }.toLongArray()
}