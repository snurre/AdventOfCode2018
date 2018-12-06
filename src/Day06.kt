import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs

class Day06 {
    data class Point(val x: Int, val y: Int)

    private val locations = File(this.javaClass.getResource("06.txt").path).readLines().map { s ->
        s.split(", ").let { p -> Point(p[0].toInt(), p[1].toInt()) }
    }

    private val distances: Array<Array<Array<Int>>> = {
        val distances = Array(locations.maxBy { it.x }!!.x) { Array(locations.maxBy { it.y }!!.y) { Array(locations.size) { 0 } } }
        for (i in 0 until locations.size) {
            for (x in 0 until distances.size) {
                for (y in 0 until distances[x].size) {
                    distances[x][y][i] = abs(locations[i].x - x) + abs(locations[i].y - y)
                }
            }
        }
        distances
    }.invoke()

    @Test
    fun part1() {
        // Map of nearest
        val infinites = mutableSetOf<Int>()
        val m = Array(distances.size) { Array(distances[0].size) { -1 } }
        for (x in 0 until distances.size) {
            for (y in 0 until distances[x].size) {
                val v = distances[x][y].sortedArray()[0]
                val nearest = mutableListOf<Int>()
                for (i in 0 until distances[x][y].size) {
                    if (distances[x][y][i] == v) nearest.add(i)
                }
                m[x][y] = if (nearest.size == 1) nearest[0] else -1
                if (x == 0 || x == distances.size - 1 || y == 0 || y == distances[x].size - 1) infinites.add(nearest[0])
            }
        }

        // Size of each nearest area
        val areas = Array(locations.size) { 0 }
        for (x in 0 until m.size) {
            for (y in 0 until m[x].size) {
                if (m[x][y] >= 0) areas[m[x][y]]++
            }
        }
        println(areas.withIndex().filter { it.index !in infinites }.sortedByDescending { it.value }[0].value)
    }

    @Test
    fun part2() {
        println(distances.sumBy { x -> x.count { it.sum() <= 10000 } })
    }
}
