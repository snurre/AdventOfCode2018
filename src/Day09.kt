import org.junit.jupiter.api.Test
import java.lang.Math.abs
import java.util.*

class Day09 {
    private val playerCount = 478
    private val lastMarbleValue = 71240L

    private fun <T> ArrayDeque<T>.rotate(d: Int): ArrayDeque<T> {
        if (d > 0) {
            for (i in 0 until d) this.addFirst(this.removeLast())
        } else if (d < 0) {
            for (i in 0 until abs(d) - 1) this.addLast(this.remove())
        }
        return this
    }

    private fun play(max: Long): Long {
        val scores = Array(playerCount) { 0L }
        val circle = ArrayDeque<Long>()
        circle.add(0L)
        for (marble in 1..max) {
            if (marble % 23 == 0L) {
                scores[marble.toInt() % scores.size] += marble + circle.rotate(-7).pop()
            } else {
                circle.rotate(2).add(marble)
            }
        }
        return scores.max()!!
    }

    @Test
    fun part1() {
        println(play(lastMarbleValue))
    }

    @Test
    fun part2() {
        println(play(lastMarbleValue * 100))
    }
}
