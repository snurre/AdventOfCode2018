import org.junit.jupiter.api.Test
import java.io.File

class Day19 {

    private val lines = File(this.javaClass.getResource("19.txt").path).readLines()
    private val ip = lines.first().removePrefix("#ip ").toInt()
    private val operations = mapOf<String, (IntArray, Int, Int, Int) -> Unit>(
        "addr" to { r, a, b, c -> r[c] = r[a] + r[b] },
        "addi" to { r, a, b, c -> r[c] = r[a] + b },
        "mulr" to { r, a, b, c -> r[c] = r[a] * r[b] },
        "muli" to { r, a, b, c -> r[c] = r[a] * b },
        "banr" to { r, a, b, c -> r[c] = r[a] and r[b] },
        "bani" to { r, a, b, c -> r[c] = r[a] and b },
        "borr" to { r, a, b, c -> r[c] = r[a] or r[b] },
        "bori" to { r, a, b, c -> r[c] = r[a] or b },
        "setr" to { r, a, b, c -> r[c] = r[a] },
        "seti" to { r, a, b, c -> r[c] = a },
        "gtir" to { r, a, b, c -> r[c] = if (a > r[b]) 1 else 0 },
        "gtri" to { r, a, b, c -> r[c] = if (r[a] > b) 1 else 0 },
        "gtrr" to { r, a, b, c -> r[c] = if (r[a] > r[b]) 1 else 0 },
        "eqir" to { r, a, b, c -> r[c] = if (a == r[b]) 1 else 0 },
        "eqri" to { r, a, b, c -> r[c] = if (r[a] == b) 1 else 0 },
        "eqrr" to { r, a, b, c -> r[c] = if (r[a] == r[b]) 1 else 0 }
    )
    private val instructions = lines.drop(1).map { it.split(" ") }.map { operations[it[0]]!! to it.drop(1).map { v -> v.toInt() } }

    private fun run(r: IntArray, loopValue: Int = 0): IntArray {
        while (r[ip] < instructions.size) {
            val (op, v) = instructions[r[ip]]
            op.invoke(r, v[0], v[1], v[2])
            if (loopValue > 0 && r[ip] == loopValue) break
            r[ip]++
        }
        return r
    }

    private fun findLoop(): Int {
        val r = listOf(1, 0, 0, 0, 0, 0).toIntArray()
        var previousLoop: String? = null
        var currentLoop = mutableListOf<Int>()
        while (r[ip] < instructions.size) {
            val prev = r[ip]
            val (op, v) = instructions[r[ip]]
            op.invoke(r, v[0], v[1], v[2])
            currentLoop.add(r[ip])
            if (r[ip] < prev) {
                if (previousLoop != null && previousLoop != currentLoop.joinToString(" ")) return prev - 1
                previousLoop = currentLoop.joinToString(" ")
                currentLoop = mutableListOf()
            }
            r[ip]++
        }
        return 0
    }

    @Test
    fun part1() {
        println(run(IntArray(6))[0])
    }

    @Test
    fun part2() {
        println(run(listOf(1, 0, 0, 0, 0, 0).toIntArray(), findLoop())[1].let { n -> (1..n).filter { n % it == 0 }.sum() })
    }
}
