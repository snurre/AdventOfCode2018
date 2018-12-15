import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Day11 {
    private val puzzleInput = 5468
    private val grid = {
        val grid = Array(300) { IntArray(300) }
        for (y in 0 until grid.size) {
            for (x in 0 until grid[y].size) {
                grid[y][x] = calculatePowerLevel(x + 1, y + 1)
            }
        }
        grid
    }.invoke()

    private fun calculatePowerLevel(x: Int, y: Int): Int {
        val rackId = x + 10
        return ((((rackId * y) + puzzleInput) * rackId / 100) % 10) - 5
    }

    private fun findMaxPower(size: Int): Triple<Int, Point, Int> {
        var bestPoint = Point(0, 0)
        var bestPowerLevelSum = Int.MIN_VALUE
        for (y in 0..grid.size - size) {
            for (x in 0..grid[y].size - size) {
                var s = 0
                for (yy in y until y + size) for (xx in x until x + size) s += grid[yy][xx]
                if (s > bestPowerLevelSum) {
                    bestPoint = Point(x + 1, y + 1)
                    bestPowerLevelSum = s
                }
            }
        }
        return Triple(size, bestPoint, bestPowerLevelSum)
    }

    @Test
    fun part1() {
        with(findMaxPower(3).second) { println("$x,$y") }
    }

    @Test
    fun part2() {
        runBlocking(Dispatchers.Default) {
            val results = mutableListOf<Deferred<Triple<Int, Point, Int>>>()
            for (s in 1..300) async { findMaxPower(s) }.let { results.add(it) }
            results.map { it.await() }.maxBy { it.third }!!.let {
                println("${it.second.x},${it.second.y},${it.first}")
            }
        }
    }
}
