import org.junit.jupiter.api.Test
import java.io.File

class Day07 {
    data class Step(val c: Char, var isComplete: Boolean = false) : Comparable<Step> {
        val requirements = mutableSetOf<Step>()
        val duration by lazy { 61 + (c - 'A') }
        var isAvailable: Boolean = false
            get() = requirements.all { it.isComplete }

        override fun compareTo(other: Step): Int = compareValuesBy(this, other) { it.c }
    }

    private val steps = File("resources/07.txt").useLines {
        val steps = mutableMapOf<Char, Step>()
        it.map { s -> s.split(' ').let { p -> p[1][0] to p[7][0] } }.forEach { p ->
            steps.computeIfAbsent(p.second) { Step(p.second) }
                .requirements.add(steps.computeIfAbsent(p.first) { Step(p.first) })
        }
        steps.values.sorted().toList()
    }

    @Test
    fun part1() {
        val result = mutableListOf<Char>()
        while (result.size < steps.size) {
            val s = steps.filter { s -> !s.isComplete && s.isAvailable }.sorted().first()
            result.add(s.c)
            s.isComplete = true
        }
        println(result.joinToString(""))
    }

    @Test
    fun part2() {
        val workers = 5
        var time = -1
        val progress = mutableMapOf<Step, Int>()
        val result = mutableListOf<Char>()
        while (result.size < steps.size) {
            ++time
            progress.filter { it.value == time }.keys.sorted().forEach { s ->
                progress.remove(s)
                s.isComplete = true
                result += s.c
            }

            steps.filter { s -> !s.isComplete && s.isAvailable && s !in progress }.sorted()
                .take(workers - progress.size).forEach { progress[it] = time + it.duration }
        }
        println(time)
    }

}
