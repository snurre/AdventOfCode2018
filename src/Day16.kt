import org.junit.jupiter.api.Test
import java.io.File

class Day16 {

    private val operations = mapOf<String, (Map<Int, Int>, IntArray) -> Int>(
            "addr" to { r, i -> r[i[1]]!! + r[i[2]]!! },
            "addi" to { r, i -> r[i[1]]!! + i[2] },
            "mulr" to { r, i -> r[i[1]]!! * r[i[2]]!! },
            "muli" to { r, i -> r[i[1]]!! * i[2] },
            "banr" to { r, i -> r[i[1]]!! and r[i[2]]!! },
            "bani" to { r, i -> r[i[1]]!! and i[2] },
            "borr" to { r, i -> r[i[1]]!! or r[i[2]]!! },
            "bori" to { r, i -> r[i[1]]!! or i[2] },
            "setr" to { r, i -> r[i[1]]!! },
            "seti" to { r, i -> i[1] },
            "gtir" to { r, i -> if (i[1] > r[i[2]]!!) 1 else 0 },
            "gtri" to { r, i -> if (r[i[1]]!! > i[2]) 1 else 0 },
            "gtrr" to { r, i -> if (r[i[1]]!! > r[i[2]]!!) 1 else 0 },
            "eqir" to { r, i -> if (i[1] == r[i[2]]!!) 1 else 0 },
            "eqri" to { r, i -> if (r[i[1]]!! == i[2]) 1 else 0 },
            "eqrr" to { r, i -> if (r[i[1]]!! == r[i[2]]!!) 1 else 0 }
    )
    private val lines = File("resources/16.txt").readLines()
    private val trainingInstructions = lines.withIndex().filter { it.value.startsWith("Before") }.map { (i, v) ->
        Triple(
                v.substring(9, 19).split(", ").map { it.toInt() }.toIntArray(),
                lines[i + 1].split(' ').map { it.toInt() }.toIntArray(),
                lines[i + 2].substring(9, 19).split(", ").map { it.toInt() }.toIntArray()
        )
    }
    private val opCodes: Map<Int, MutableSet<String>> = trainingInstructions.fold(mutableMapOf()) { o, (before, instruction, after) ->
        operations.forEach { (name, operation) ->
            val register = before.withIndex().map { it.index to it.value }.toMap().toMutableMap()
            register[instruction[3]] = operation.invoke(register, instruction)
            if (register.values.toIntArray().contentEquals(after)) o.computeIfAbsent(instruction[0]) { mutableSetOf() }.add(name)
        }
        o
    }

    @Test
    fun part1() {
        println(trainingInstructions.count { (_, instruction, _) -> opCodes[instruction[0]]!!.size >= 3 })
    }

    @Test
    fun part2() {
        val possibleOperations = opCodes.map { it.key to it.value.toMutableSet() }.toMap().toMutableMap()
        val realOperations = mutableMapOf<Int, String>()
        while (possibleOperations.isNotEmpty()) {
            for (op in possibleOperations.filter { it.value.size == 1 }) {
                val o = op.value.first()
                realOperations[op.key] = o
                possibleOperations.values.forEach { it.remove(o) }
                possibleOperations.remove(op.key)
            }
        }
        val registers = mutableMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)
        for (instruction in lines.takeLast(lines.size - lines.withIndex().filter { it.value.startsWith("After") }.maxBy { it.index }!!.index + 4).map { it.split(' ').map { it.toInt() }.toIntArray() }) {
            registers[instruction[3]] = operations[realOperations[instruction[0]]]!!.invoke(registers, instruction)
        }
        println(registers[0])
    }

}
