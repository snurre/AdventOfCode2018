import org.junit.jupiter.api.Test
import java.io.File
import java.util.*


class Day17 {
    private val sand = ' '
    private val clay = '#'
    private val runningWater = '|'
    private val settledWater = '~'
    private val map: Array<CharArray>

    init {
        fun String.toIntRange() = this.split('=')[1].split("..").map { r -> r.toInt() }.let { r -> r[0]..(if (r.size > 1) r[1] else r[0]) }
        val clayTiles = File(this.javaClass.getResource("17.txt").path).readLines().map { it.split(", ").sorted().let { p -> p[0].toIntRange() to p[1].toIntRange() } }

        val size = Point(clayTiles.maxBy { c -> c.first.last }!!.first.last, clayTiles.maxBy { c -> c.second.last }!!.second.last)
        map = Array(size.y + 2) { CharArray(size.x + 2) { ' ' } }
        for (c in clayTiles) for (y in c.second) for (x in c.first) map[y][x] = '#'

        val queue = ArrayDeque<Point>()
        queue += Point(500, 0)

        fun add(p: Point) {
            if (p.y <= size.y) queue += p
        }

        while (queue.isNotEmpty()) {
            val p = queue.poll()
            if (p.isSettledWater()) continue
            when {
                p.down.isSand() -> {
                    map[p.y][p.x] = runningWater
                    add(p.down)
                }
                p.down.isClay() || p.down.isSettledWater() -> {
                    map[p.y][p.x] = runningWater
                    if (p.isRowFull()) {
                        var c = p
                        while (!c.isClay()) {
                            map[c.y][c.x] = settledWater
                            if (c.up.isRunningWater()) add(c.up)
                            c = c.left
                        }
                        c = p.right
                        while (!c.isClay()) {
                            map[c.y][c.x] = settledWater
                            if (c.up.isRunningWater()) add(c.up)
                            c = c.right
                        }
                    } else {
                        if (p.right.isSand()) add(p.right)
                        if (p.right.isRunningWater() && p.right.right.isSand()) add(p.right.right)
                        if (p.left.isSand()) add(p.left)
                        if (p.left.isRunningWater() && p.left.left.isSand()) add(p.left.left)
                    }
                }
            }
        }
    }

    private fun Point.isSand() = map[this.y][this.x] == sand
    private fun Point.isClay() = map[this.y][this.x] == clay
    private fun Point.isSettledWater() = map[this.y][this.x] == settledWater
    private fun Point.isRunningWater() = map[this.y][this.x] == runningWater
    private fun Point.isRowFull(): Boolean {
        var c = this
        while (!c.left.isClay()) {
            if (!c.left.isSettledWater() && !c.left.isRunningWater()) return false
            c = c.left
        }
        c = this
        while (!c.right.isClay()) {
            if (!c.right.isSettledWater() && !c.right.isRunningWater()) return false
            c = c.right
        }
        return true
    }

    @Test
    fun part1() {
        println(map.sumBy { row -> row.count { c -> c == settledWater || c == runningWater } } + 1)
    }

    @Test
    fun part2() {
        println(map.sumBy { row -> row.count { c -> c == settledWater } })
    }
}

