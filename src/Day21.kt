import org.junit.jupiter.api.Test
import java.io.File

class Day21 {

    private val lines = File("resources/21.txt").readLines()
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
    private val breakCondition: Int
    private val breakConditionRegister: Int

    init {
        val r = mutableMapOf<Int, Int>()
        fun s(a: Int, b: Int? = null) {
            r[a] = (r[a] ?: 0) + 1
            if (b != null) r[b] = (r[b] ?: 0) + 1
        }

        val twoRegisterOps = setOf("addr", "mulr", "banr", "borr", "gtrr", "eqrr")
        val aRegisterOps = setOf("addi", "muli", "bani", "bori", "setr", "gtri", "eqri")
        val bRegisterOps = setOf("gtir", "eqir")
        for (i in lines.drop(1).map { it.split(" ") }) {
            val v = i.drop(1).map { it.toInt() }
            when {
                i[0] in twoRegisterOps -> s(v[0], v[1])
                i[0] in aRegisterOps -> s(v[0])
                i[0] in bRegisterOps -> s(v[1])
            }
        }
        breakConditionRegister = r.filter { it.value == 1 }.keys.first()

        var breakCondition = 0
        for (i in lines.drop(1).map { it.split(" ") }.withIndex()) {
            val v = i.value.drop(1).map { it.toInt() }
            when {
                i.value[0] in twoRegisterOps && (v[0] == breakConditionRegister || v[1] == breakConditionRegister) -> breakCondition = i.index
                i.value[0] in aRegisterOps && v[0] == breakConditionRegister -> breakCondition = i.index
                i.value[0] in bRegisterOps && v[1] == breakConditionRegister -> breakCondition = i.index
            }
        }
        this.breakCondition = breakCondition
    }

    @Test
    fun part1() {
        val r = IntArray(6)
        while (r[ip] < instructions.size) {
            val (op, v) = instructions[r[ip]]
            if (r[ip] == breakCondition) {
                println("${r[v[breakConditionRegister]]}")
                break
            }
            op.invoke(r, v[0], v[1], v[2])
            r[ip]++
        }
    }

    @Test
    fun part2() {
        val r = IntArray(6)
        val haltingValues = mutableListOf<Int>()
        while (r[ip] < instructions.size) {
            val (op, v) = instructions[r[ip]]
            if (r[ip] == breakCondition) {
                if (r[v[breakConditionRegister]] in haltingValues) {
                    println(haltingValues.last())
                    break
                }
                haltingValues.add(r[v[breakConditionRegister]])
            }
            op.invoke(r, v[0], v[1], v[2])
            r[ip]++
        }
    }

    @Test
    fun reversed() {
        val haltingValues = mutableSetOf<Int>()
        var first = 0
        var last = 0
        var c = 0
        while (true) {
            var f = c or 65536
            c = 2238642
            while (f > 0) {
                c = (((c + (f and 255)) and 16777215) * 65899) and 16777215
                f = f shr 8
            }
            if (haltingValues.isEmpty()) first = c
            if (!haltingValues.add(c)) break
            last = c
        }
        println(first)
        println(last)
    }
}
