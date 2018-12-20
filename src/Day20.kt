import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.math.min

class Day20 {
    private val rooms: Map<Point, Int>

    init {
        var cur = Point(0, 0)
        rooms = mutableMapOf(cur to 0)
        val queue = ArrayDeque<Point>()
        for (c in File(this.javaClass.getResource("20.txt").path).readText()) {
            when (c) {
                '(' -> queue.add(cur)
                ')' -> cur = queue.pollLast()
                '|' -> cur = queue.peekLast()
                'N', 'E', 'S', 'W' -> {
                    val next = cur.walk(c)
                    rooms[next] = min(rooms[next] ?: rooms[cur]!!+1, rooms[cur]!! + 1)
                    cur = next
                }
            }
        }
    }

    @Test
    fun part1() {
        println(rooms.maxBy { it.value }!!.value)
    }

    @Test
    fun part2() {
        println(rooms.filter { it.value >= 1000 }.count())
    }
}
