import org.junit.jupiter.api.Test
import java.io.File

class Day01 {
    private fun getNumbers(): List<Int> {
        return File("resources/01.txt").useLines { it.map(String::toInt).toList() }
    }

    @Test
    fun part1() {
        println(getNumbers().sum())
    }

    @Test
    fun part2() {
        val numbers = getNumbers()
        var frequency = 0
        val frequencies = hashSetOf<Int>()
        while (true)
            for (delta in numbers) {
                frequency += delta
                if (!frequencies.add(frequency)) {
                    println(frequency)
                    return
                }
            }
    }
}
