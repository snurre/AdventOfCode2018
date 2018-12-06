import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs

class Day06 {
    data class Point(val x: Int, val y: Int)

    private val locations = File(this.javaClass.getResource("06.txt").path).readLines().map { s ->
        s.split(", ").let { p -> Point(p[0].toInt(), p[1].toInt()) }
    }

    private fun getDistanceMatrix(): Array<Array<Array<Int>>> {
        val distances = Array(locations.maxBy { it.x }!!.x) { Array(locations.maxBy { it.y }!!.y) { Array(locations.size) { 0 } } }
        for (i in 0 until locations.size) {
            for (x in 0 until distances.size) {
                for (y in 0 until distances[x].size) {
                    distances[x][y][i] = abs(locations[i].x - x) + abs(locations[i].y - y)
                }
            }
        }
        return distances
    }

    @Test
    fun part1() {
        val distances = getDistanceMatrix()

        // Map of nearest
        val infinites = mutableSetOf<Int>()
        val m = Array(distances.size) { Array(distances[0].size) { -1 } }
        distances.withIndex().forEach { x ->
            x.value.withIndex().forEach { y ->
                val c = y.value
                val min = c.sortedArray()[0]
                val nearest = c.withIndex().filter { it.value == min }.map { it.index }
                m[x.index][y.index] = if (nearest.size == 1) nearest[0] else -1
                if (x.index == 0 || x.index == distances.size - 1 || y.index == 0 || y.index == x.value.size - 1) infinites.add(nearest[0])
            }
        }

        // Size of each nearest area
        val areas = Array(locations.size) { 0 }
        m.forEach { x ->
            x.filter { it >= 0 }.forEach { y ->
                areas[y]++
            }
        }
        println(areas.withIndex().filter { it.index !in infinites }.sortedByDescending { it.value }[0].value)
    }

    @Test
    fun part2() {
        val distances = getDistanceMatrix()
        var size = 0
        distances.withIndex().forEach { x ->
            x.value.withIndex().forEach { y ->
                if (distances[x.index][y.index].sum() < 10000) size++
            }
        }
        println(size)
    }
}
