import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.max
import kotlin.math.min

class Day18 {
    private val open = '.'
    private val tree = '|'
    private val lumberyard = '#'
    private val puzzleInput = File(this.javaClass.getResource("18.txt").path).readLines()

    private fun solve(minutes: Int): Int {
        var map = puzzleInput
        val seen = mutableMapOf(map to 0)
        var t = -1
        while (++t < minutes) {
            val m = seen.computeIfAbsent(map) { t }
            if (m < t) {
                t += (minutes - t) % (t - m)
            }
            map = map.mapIndexed { y, row ->
                row.withIndex().joinToString("") { (x, c) ->
                    val s = (max(0, y - 1)..min(map.size - 1, y + 1))
                        .flatMap { yy -> (max(0, x - 1)..min(row.length - 1, x + 1)).map { xx -> map[yy][xx] } }
                    when (c) {
                        open -> if (s.count { it == tree } >= 3) tree else open
                        tree -> if (s.count { it == lumberyard } >= 3) lumberyard else tree
                        lumberyard -> if (s.count { it == lumberyard } >= 1 && s.count { it == tree } >= 1) lumberyard else open
                        else -> c
                    }.toString()
                }
            }
        }
        return map.sumBy { row -> row.count { it == tree } } * map.sumBy { row -> row.count { it == lumberyard } }
    }

    @Test
    fun part1() {
        println(solve(10))
    }

    @Test
    fun part2() {
        println(solve(1000000000))
    }
}
