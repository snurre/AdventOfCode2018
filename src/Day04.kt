import org.junit.jupiter.api.Test
import java.io.File

class Day04 {
    private val guards: HashMap<Int, Array<Int>> = {
        val guards = hashMapOf<Int, Array<Int>>()
        val pattern = Regex("Guard #([0-9]+) begins shift")
        var currentGuard = 0
        var currentStart = 0

        File(this.javaClass.getResource("04.txt").path).readLines().sorted().map { s ->
            val currentMinute = s.substring(15, 17).toInt()
            val event = s.substring(19, s.length)
            when (event) {
                "falls asleep" -> currentStart = currentMinute
                "wakes up" -> {
                    val minutes = guards.computeIfAbsent(currentGuard) { Array(60) { 0 } }
                    for (m in currentStart until currentMinute) {
                        minutes[m]++
                    }
                }
                else -> currentGuard = pattern.matchEntire(event)!!.groupValues[1].toInt()
            }
        }
        guards
    }.invoke()

    @Test
    fun part1() {
        val sleepiestGuard = guards.maxBy { g -> g.value.sum() }!!
        val sleepiestMinute = sleepiestGuard.value.withIndex().maxBy { it.value }!!.index
        println("${sleepiestGuard.key} * $sleepiestMinute = ${sleepiestGuard.key * sleepiestMinute}")
    }

    @Test
    fun part2() {
        val sleepiestMinute = guards.flatMap { g ->
            g.value.withIndex().map { m ->
                Triple(g.key, m.index, m.value)
            }
        }.maxBy { it.third }!!
        println("${sleepiestMinute.first} * ${sleepiestMinute.second} = ${sleepiestMinute.first * sleepiestMinute.second}")
    }

}
