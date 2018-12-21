import org.junit.jupiter.api.Test
import java.io.File

class Day12 {
    private val initialState = "##..##..#.##.###....###.###.#.#.######.#.#.#.#.##.###.####..#.###...#######.####.##...#######.##..#"
    private val rules: Map<CharSequence, CharSequence> = File("resources/12.txt").readLines().associate { it.split(" => ", limit = 2).let { p -> p[0] to p[1] } }

    private fun solve(n: Long): Long {
        val states = mutableMapOf<String, Pair<Long, Long>>()
        var state = initialState
        var offset = 0L
        var i = 0L
        while (i < n) {
            states[state] = i to offset
            state = "....$state....".windowed(5) { rules.getOrElse(it) { "." } }
                .joinToString(separator = "")
                .dropLastWhile { it != '#' }
                .apply { offset += indexOf('#') - 2 }
                .dropWhile { it != '#' }
            i++
            val (previousI, previousOffset) = states[state] ?: continue
            offset += (n - i) / (i - previousI) * (offset - previousOffset)
            i = n - (n - i) % (i - previousI)
        }
        return state.withIndex().fold(0L) { acc, (i, c) -> if (c == '#') acc + offset + i else acc }
    }

    @Test
    fun part1() {
        println(solve(20))
    }

    @Test
    fun part2() {
        println(solve(50000000000))
    }
}
