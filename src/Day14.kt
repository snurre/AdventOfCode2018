import org.junit.jupiter.api.Test

class Day14 {
    private val puzzleInput = 765071

    @Test
    fun part1() {
        val scores = mutableListOf(3, 7)
        var i = 0 to 1
        for (j in 0 until puzzleInput + 10) {
            scores += (scores[i.first] + scores[i.second]).toString().map(Character::getNumericValue)
            i = ((i.first + scores[i.first] + 1) % scores.size) to ((i.second + scores[i.second] + 1) % scores.size)
        }
        println(scores.subList(puzzleInput, puzzleInput + 10).joinToString(""))
    }

    @Test
    fun part2() {
        val scores = mutableListOf(3, 7)
        var i = 0 to 1
        while (puzzleInput.toString() !in scores.takeLast(10).joinToString("")) {
            scores += (scores[i.first] + scores[i.second]).toString().map(Character::getNumericValue)
            i = ((i.first + scores[i.first] + 1) % scores.size) to ((i.second + scores[i.second] + 1) % scores.size)
        }
        println(scores.joinToString("").indexOf(puzzleInput.toString()))
    }
}
