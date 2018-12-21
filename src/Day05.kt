import org.junit.jupiter.api.Test
import java.io.File

class Day05 {
    private val data = File("resources/05.txt").readText().trim()
    private val units = ('a'..'z').let {
        val s = hashSetOf<String>()
        it.forEach { c ->
            s.add("$c${c.toUpperCase()}")
            s.add("${c.toUpperCase()}$c")
        }
        s
    }

    private fun react(s: String): String {
        val sb = StringBuilder()
        var i = -1
        while (++i < s.length - 1) {
            if (s.substring(i, i + 2) in units) i++ else sb.append(s[i])
            if (i == s.length - 2) sb.append(s[i] + 1)
        }
        return sb.toString()
    }

    private fun solve(s: String): String {
        var polymer = s
        while (polymer != { val p = react(polymer); polymer = p;p }()) {}
        return polymer
    }

    @Test
    fun part1() {
        println(solve(data).length)
    }

    @Test
    fun part2() {
        var shortest = data.length
        for (c in 'a'..'z') {
            val polymer = solve(data.replace(c.toString(), "", true))
            if (polymer.length < shortest) shortest = polymer.length
        }
        println(shortest)
    }
}
