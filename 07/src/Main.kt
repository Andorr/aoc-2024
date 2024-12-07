import java.io.File

fun main() {
    val input = parse("input.txt")

    println("Part01 ${part01(input)}")
    println("Part02 ${part02(input)}")
}

fun part01(input: List<Calibration>): Long = input.calibrate(Operator.Add, Operator.Mul)
fun part02(input: List<Calibration>): Long = input.calibrate(Operator.Add, Operator.Mul, Operator.Concat)

fun List<Calibration>.calibrate(vararg operators: Operator) = this.sumOf {
    if (it.evaluate(operators.toList())) it.testValue else 0L
}

fun parse(fileName: String): List<Calibration>
{
    return File(fileName).readLines()
        .map { it.split(":")
            .let {
                Calibration(
                    it.first().toLong(),
                    it.last().trim().split(" ").map { it.toLong() }.toList()
                )
            }
        }
}

enum class Operator(val operator: (Long, Long) -> Long)
{
    Mul({ a, b -> a * b}),
    Add({ a, b -> a + b}),
    Concat({ a, b -> (a.toString() + b.toString()).toLong()})
}

data class Calibration(val testValue: Long, val numbers: List<Long>)
{
    fun evaluate(operators: List<Operator>): Boolean
    {
        data class State(val value: Long, val i: Int)
        val queue = mutableListOf<State>()
        queue.add(State(numbers[0], 0))

        while(queue.isNotEmpty())
        {
            val state = queue.removeLast()
            if(state.value == testValue && state.i == numbers.size-1)
            {
                return true
            }

            if(state.i < numbers.size-1)
            {
                operators.map {
                    State(it.operator(state.value, numbers[state.i + 1]), state.i + 1)
                }.also { queue.addAll(it) }
            }
        }
        return false
    }
}
