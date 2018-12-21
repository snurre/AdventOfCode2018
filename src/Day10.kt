import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day10 {
    data class Light(val start: Point, val velocity: Point) {
        fun getPos(t: Int): Point = Point(start.x + (velocity.x) * t, start.y + (velocity.y) * t)
    }

    val rx = Regex("position=< *(-?[0-9]+), *([-?0-9]+)> velocity=< *(-?[0-9]+), *([-?0-9]+)>")
    private val lights = File("resources/10.txt").readLines().map { s ->
        val m = rx.matchEntire(s)!!
        Light(
            Point(m.groupValues[1].toInt(), m.groupValues[2].toInt()),
            Point(m.groupValues[3].toInt(), m.groupValues[4].toInt())
        )
    }

    private fun calculateArraySize(t: Int): Pair<Point, Point> {
        var min = Point(Int.MAX_VALUE, Int.MAX_VALUE)
        var max = Point(Int.MIN_VALUE, Int.MIN_VALUE)
        for (light in lights) {
            val p = light.getPos(t)
            min = Point(min(p.x, min.x), min(p.y, min.y))
            max = Point(max(p.x, max.x), max(p.y, max.y))
        }
        return min to max
    }

    private fun getTimeOfSmallestSky(): Int {
        val sizes = mutableMapOf<Int, Long>()
        for (t in 0..20000) {
            val (a, b) = calculateArraySize(t)
            val size = 1L * abs(b.x - a.x) * abs(b.y - a.y)
            if (size <= 1000) sizes[t] = size
        }
        return sizes.minBy { it.value }!!.key
    }

    @Test
    fun part1() {
        val t = getTimeOfSmallestSky()
        val (min, max) = calculateArraySize(t)
        val sky = Array(abs(max.y - min.y) + 1) { Array(abs(max.x - min.x) + 1) { false } }
        for (light in lights.map { it.getPos(t) }) sky[light.y - min.y][light.x - min.x] = true
        for (row in sky) println(row.map { if (it) '#' else ' ' }.joinToString(""))
    }

    @Test
    fun part2() {
        println(getTimeOfSmallestSky())
    }
}
