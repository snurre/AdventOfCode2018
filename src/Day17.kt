import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

private const val Sand = ' '
private const val Clay = '#'
private const val RunningWater = '|'
private const val SettledWater = '~'

class Day17 {
    private val top: Point
    private val bottom: Point
    private val map: Array<CharArray>

    init {
        val clayTiles = File(this.javaClass.getResource("17.txt").path).readLines().map { s ->
            fun String.toIntRange(): IntRange =
                this.split('=')[1].split("..").map { r -> r.toInt() }.let { r -> r[0]..(if (r.size > 1) r[1] else r[0]) }
            s.split(", ").sorted().let { p ->
                p[0].toIntRange() to p[1].toIntRange()
            }
        }

        var top = Point(Int.MAX_VALUE, 0)
        var bottom = Point(Int.MIN_VALUE, Int.MIN_VALUE)
        for (clay in clayTiles) {
            if (clay.first.min()!! < top.x) top = Point(clay.first.min()!!, top.y)
            if (clay.first.max()!! > bottom.x) bottom = Point(clay.first.max()!!, bottom.y)
            if (clay.second.max()!! > bottom.y) bottom = Point(bottom.x, clay.second.max()!!)
        }
        val map = Array(bottom.y - top.y + 2) { CharArray(bottom.x - top.x + 2) { ' ' } }
        map[0][500 - top.x] = '+'
        for (c in clayTiles) {
            for (y in c.second) {
                for (x in c.first) {
                    map[y - top.y][x - top.x] = '#'
                }
            }
        }
        this.top = top
        this.bottom = bottom
        this.map = map

        val queue = ArrayDeque<Point>()
        queue += Point(500 - top.x, 1)

        fun add(p: Point) {
            if (p.y in top.y until bottom.y) queue += p
        }

        while (queue.isNotEmpty()) {
            val p = queue.poll()
            if (p.isSettledWater()) continue
            when {
                p.down.isSand() -> {
                    map[p.y][p.x] = RunningWater
                    add(p.down)
                }
                p.down.isClay() || p.down.isWater() -> {
                    map[p.y][p.x] = RunningWater
                    if (p.isRowFull()) {
                        for (c in p.getFilledRow()) {
                            map[c.y][c.x] = SettledWater
                            if (c.up.isRunningWater()) add(c.up)
                        }
                    }
                    if (p.right.isSand()) add(p.right)
                    if (p.right.isRunningWater() && p.right.right.isSand()) add(p.right.right)
                    if (p.left.isSand()) add(p.left)
                    if (p.left.isRunningWater() && p.left.left.isSand()) add(p.left.left)
                }
            }
        }
    }

    private fun Point.isSand() = map[this.y][this.x] == Sand
    private fun Point.isClay() = map[this.y][this.x] == Clay
    private fun Point.isWater() = this.isSettledWater() || this.isRunningWater()
    private fun Point.isSettledWater() = map[this.y][this.x] == SettledWater
    private fun Point.isRunningWater() = map[this.y][this.x] == RunningWater
    private fun Point.isRowFull(): Boolean {
        if (!this.isWater() || !this.down.isClay() && !this.down.isSettledWater()) return false
        var c = this
        while (!c.left.isClay()) {
            if (!c.left.isWater()) return false
            c = c.left
        }
        c = this
        while (!c.right.isClay()) {
            if (!c.right.isWater()) return false
            c = c.right
        }
        return true
    }

    private fun Point.getFilledRow(): List<Point> {
        if (!this.isWater() && !(this.down.isClay() || !this.down.isSettledWater())) return Collections.emptyList()
        val row = mutableListOf<Point>()
        var c = this
        while (true) {
            if (c.left.isClay()) break
            c = c.left
        }
        while (true) {
            row += c
            if (c.right.isClay()) break
            c = c.right
        }
        return row
    }

    @Test
    fun part1() {
        println(map.sumBy { row -> row.count { c -> c == SettledWater || c == RunningWater } })
    }

    @Test
    fun part2() {
        println(map.sumBy { row -> row.count { c -> c == SettledWater } })
    }
}

