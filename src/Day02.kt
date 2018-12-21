import org.junit.jupiter.api.Test
import java.io.File

class Day02 {
    private fun getBoxIds(): List<String> {
        return File("resources/03.txt").readLines()
    }

    @Test
    fun part1() {
        var two = 0
        var three = 0
        getBoxIds().forEach { boxId ->
            val chars = hashMapOf<Char, Int>()
            boxId.forEach { c -> chars[c] = (chars[c] ?: 0) + 1 }
            var hasTwo = false
            var hasThree = false
            chars.values.forEach { count ->
                when (count) {
                    2 -> hasTwo = true
                    3 -> hasThree = true
                }
            }
            if (hasTwo) two++
            if (hasThree) three++
        }
        println("$two * $three = ${two * three}")
    }

    @Test
    fun part2() {
        val boxIds = getBoxIds()
        for (i in 0 until boxIds[0].length) {
            boxIds.forEach { a ->
                boxIds.forEach { b ->
                    if (a != b) {
                        val charsA = a.toCharArray()
                        val charsB = b.toCharArray()
                        var isMatch = true
                        for (j in 0 until charsA.size) {
                            if (j != i && charsA[j] != charsB[j]) {
                                isMatch = false
                                break
                            }
                        }
                        if (isMatch) {
                            println("$i\n$a\n$b\n${a.toCharArray().filterIndexed { index, _ -> index != i }.joinToString("")}")
                            return
                        }
                    }
                }
            }
        }
    }
}

