import org.junit.jupiter.api.Test
import java.io.File

class Day03 {
    data class Claim(
        val id: Int,
        val location: Point,
        val size: Point
    )

    private fun getClaims(): List<Claim> {
        val rx = Regex("#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)")
        return File(this.javaClass.getResource("03.txt").path).useLines {
            it.map { s ->
                val m = rx.matchEntire(s)!!
                Claim(
                    m.groupValues[1].toInt(),
                    Point(m.groupValues[2].toInt(), m.groupValues[3].toInt()),
                    Point(m.groupValues[4].toInt(), m.groupValues[5].toInt())
                )
            }.toList()
        }
    }

    private fun getSquare(claims: Collection<Claim>): Array<Array<ArrayList<Claim>>> {
        var width = 0
        var height = 0
        claims.forEach { c ->
            val currWidth = c.location.x + c.size.x
            val currHeight = c.location.y + c.location.y
            if (currWidth > width) width = currWidth
            if (currHeight > height) height = currHeight
        }
        val size = Math.max(width, height)
        return Array(size) { Array(size) { arrayListOf<Claim>() } }
    }

    private fun getClaimedSquare(claims: Collection<Claim>): Array<Array<ArrayList<Claim>>> {
        val square = getSquare(claims)
        claims.forEach { c ->
            for (x in c.location.x until c.location.x + c.size.x) {
                for (y in c.location.y until (c.location.y + c.size.y)) {
                    square[x][y].add(c)
                }
            }
        }
        return square
    }

    @Test
    fun part1() {
        val square = getClaimedSquare(getClaims())
        var overlaps = 0
        for (x in 0 until square.size) {
            for (y in 0 until square[x].size) {
                if (square[x][y].size >= 2) overlaps++
            }
        }
        println(overlaps)
    }

    @Test
    fun part2() {
        val claims = getClaims()
        val square = getClaimedSquare(claims)

        println(claims.find { c ->
            for (x in c.location.x until c.location.x + c.size.x) {
                for (y in c.location.y until c.location.y + c.size.y) {
                    if (square[x][y].size > 1) return@find false
                }
            }
            true
        })
    }
}
