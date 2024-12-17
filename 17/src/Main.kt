import java.io.File
import java.util.PriorityQueue

fun main(args: Array<String>) {
    val input = parse(args.firstOrNull() ?: "input.txt")

    println("Part01: ${part01(input)}")
    println("Part02: ${part02(input)}")
}

fun part01(input: Problem): String = input.computer.run(input.program).joinToString(",")

fun part02(input: Problem): Long {
    // Program Summary:
    // b = a%8
    // b = b ^ 7
    // c = a >> 5
    // b = b ^ 7
    // b = b ^ c
    // a = a >> 3
    // output b
    // jmp 0
    val program = input.program
    val shortProgram = program.slice(0..<program.size-2).toLongArray()

    val queue = PriorityQueue<Pair<Long, Int>>({a, b -> a.first.compareTo(b.first)})
    queue.add(Pair(0, program.size - 1))

    while(queue.isNotEmpty()) {
        val (a, i) = queue.poll()

        for (k in 0..<8) {
            val possibleA = (a shl 3) + k

            val computer = Computer(possibleA, 0, 0)
            val output = computer.run(shortProgram)[0]
            if(output != program[i]) {
                continue
            }

            if(i == 0) {
                return possibleA
            }

            queue.add(Pair(possibleA, i - 1))
        }
    }

    return -1
}

data class Problem(val computer: Computer, val program: Program)
fun parse(fileName: String): Problem {
    return File(fileName).readLines()
        .let {
            val r = Regex("\\d+")
            Problem(
                Computer(
                    r.find(it[0])!!.let { it.value.toLong() },
                    r.find(it[1])!!.let { it.value.toLong() },
                    r.find(it[2])!!.let { it.value.toLong() },
                ),
                it[4].removePrefix("Program: ").split(",").map { it.toLong() }.toLongArray()
            )
        }
}

typealias Program = LongArray

data class ExecutionContext(val program: Program, var pointer: Int)
data class Computer(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long
) {

    fun run(p: Program): LongArray {
        val ctx = ExecutionContext(p, 0)
        val output = mutableListOf<Long>()

        while(ctx.pointer < ctx.program.size) {
            val instruction = ctx.program[ctx.pointer]
            val operand = ctx.program[ctx.pointer + 1]
            when(instruction) {
                Instruction.ADV.opcode -> {
                    this.registerA = this.registerA shr operand.combo(this).toInt()
                }
                Instruction.BXL.opcode -> {
                    this.registerB = this.registerB xor operand
                }
                Instruction.BST.opcode -> {
                    this.registerB = operand.combo(this)%8L
                }
                Instruction.JNZ.opcode -> {
                    if(this.registerA != 0L) {
                        ctx.pointer = operand.toInt()
                        continue
                    }
                }
                Instruction.BXC.opcode -> {
                    this.registerB = (this.registerB xor this.registerC)
                }
                Instruction.OUT.opcode -> {
                    output.add(operand.combo(this)%8L)
                }
                Instruction.BDV.opcode -> {
                    this.registerB = this.registerA shr operand.combo(this).toInt()
                }
                Instruction.CDV.opcode -> {
                    this.registerC = this.registerA shr operand.combo(this).toInt()
                }
            }

            ctx.pointer += 2
        }

        return output.toLongArray()
    }
}


fun Long.combo(computer: Computer): Long {
    return when(this) {
        in 0..3 -> this
        4L -> computer.registerA
        5L -> computer.registerB
        6L -> computer.registerC
        else -> throw IllegalArgumentException()
    }
}

enum class Instruction(val opcode: Long) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7),
}
