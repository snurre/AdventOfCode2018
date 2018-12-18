import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.max
import kotlin.math.min

class Day18 {
    private val open = '.'
    private val tree = '|'
    private val lumberyard = '#'
    private val puzzleInput = File(this.javaClass.getResource("18.txt").path).readLines().map { it.toList() }

    private fun solve(minutes: Int): Int {
        var map = puzzleInput
        val seen = mutableMapOf(map to 0)
        var firstRepeat: List<List<Char>>? = null
        var t = 0
        while (++t <= minutes) {
            map = map.mapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    // This is slower than a nested loop, but so much cooler
                    val (trees, lumberyards) = (max(y - 1, 0)..min(y + 1, map.size - 1))
                        .flatMap { yy -> (max(x - 1, 0)..min(x + 1, map[y].size - 1)).map { xx -> yy to xx } }
                        .filter { it != y to x }
                        .groupingBy { (yy, xx) -> map[yy][xx] }
                        .eachCount()
                        .let { (it[tree] ?: 0) to (it[lumberyard] ?: 0) }
                    when (c) {
                        open -> if (trees >= 3) tree else open
                        tree -> if (lumberyards >= 3) lumberyard else tree
                        lumberyard -> if (lumberyards >= 1 && trees >= 1) lumberyard else open
                        else -> c
                    }
                }
            }
            if (firstRepeat != null) {
                if (map == firstRepeat) {
                    val cycle = (t - seen[map]!!)
                    t += ((minutes - t) / cycle) * cycle
                }
            } else if (seen.putIfAbsent(map, t) != null) {
                firstRepeat = map
            }
        }
        return map.sumBy { row -> row.count { it == tree } } * map.sumBy { row -> row.count { it == lumberyard} }
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
