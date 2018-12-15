import org.junit.jupiter.api.Test
import java.io.File

class Day13 {
    enum class Turn {
        Left,
        Straight,
        Right;

        val next by lazy {
            when (this) {
                Left -> Straight
                Straight -> Right
                Right -> Left
            }
        }
    }

    enum class D(val dx: Int, val dy: Int, val c: Char) {
        N(0, -1, '^'),
        E(1, 0, '>'),
        S(0, 1, 'v'),
        W(-1, 0, '<');

        companion object {
            fun of(c: Char): D = when (c) {
                N.c -> N
                E.c -> E
                S.c -> S
                W.c -> W
                else -> throw RuntimeException("")
            }
        }

        fun turn(t: Turn): D = when (this) {
            N -> if (t == Turn.Left) W else if (t == Turn.Right) E else this
            E -> if (t == Turn.Left) N else if (t == Turn.Right) S else this
            S -> if (t == Turn.Left) E else if (t == Turn.Right) W else this
            W -> if (t == Turn.Left) S else if (t == Turn.Right) N else this
        }

        fun next(c: Char): D = when (this) {
            N -> if (c == '/') E else if (c == '\\') W else this
            E -> if (c == '/') N else if (c == '\\') S else this
            S -> if (c == '/') W else if (c == '\\') E else this
            W -> if (c == '/') S else if (c == '\\') N else this
        }
    }

    data class Cart(
        val id: Int,
        var location: Point,
        var direction: D,
        var nextTurnDirection: Turn = Turn.Left
    ) :
        Comparable<Cart> {
        override fun compareTo(other: Cart): Int {
            return compareValuesBy(this, other) { it.location }
        }

        fun move(grid: Array<Array<Char>>) {
            location = Point(location.x + direction.dx, location.y + direction.dy)
            val c = grid[location.y][location.x]
            if (c == '+') {
                direction = direction.turn(nextTurnDirection)
                nextTurnDirection = nextTurnDirection.next
            } else {
                direction = direction.next(c)
            }
        }
    }

    private val grid: Array<Array<Char>>
    private val carts: List<Cart>

    init {
        val t = File(this.javaClass.getResource("13.txt").path).readLines()
        val g = Array(t.size) { Array(t[0].length) { ' ' } }
        val v = setOf('\\', '|', '/', '+')
        val h = setOf('\\', '-', '/', '+')
        var id = 0
        val tmpCarts = mutableListOf<Cart>()
        for (y in 0 until t.size) {
            for (x in 0 until t[y].length) {
                val c = t[y][x]
                when (c) {
                    D.N.c, D.E.c, D.S.c, D.W.c -> {
                        tmpCarts.add(Cart(id++, Point(x, y), D.of(c)))
                        if (y > 0 && y < t.size - 1 && x > 0 && x < t[y].length - 1 && t[y - 1][x] in v && t[y + 1][x] in v && t[y][x - 1] in h && t[y][x + 1] in h) {
                            g[y][x] = '+'
                        } else if (y > 0 && y < t.size - 1 && t[y - 1][x] in v && t[y + 1][x] in v) {
                            g[y][x] = '|'
                        } else if (x > 0 && x < t[y].length - 1 && t[y][x - 1] in h && t[y][x + 1] in h) {
                            g[y][x] = '-'
                        } else if ((g[y - 1][x] in v && t[y][x - 1] in h) || (g[y + 1][x] in v && t[y][x + 1] in h)) {
                            g[y][x] = '/'
                        } else if ((g[y - 1][x] in v && t[y][x + 1] in h) || (g[y + 1][x] in v && t[y][x - 1] in h)) {
                            g[y][x] = '\\'
                        }
                    }
                    else -> g[y][x] = c
                }
            }
        }
        this.grid = g
        this.carts = tmpCarts
    }

    @Test
    fun part1() {
        while (true) {
            val locations = mutableMapOf<Point, MutableSet<Cart>>()
            for (cart in carts.sortedBy { it.location }) {
                locations.computeIfAbsent(cart.location) { mutableSetOf() }.add(cart)
                cart.move(grid)
                locations.computeIfAbsent(cart.location) { mutableSetOf() }.add(cart)
            }
            val crashes = locations.entries.filter { it.value.size >= 2 }.flatMap { it.value }.toSet().toList()
            if (crashes.isNotEmpty()) {
                crashes[0].let { println("${it.location.x},${it.location.y}") }
                break
            }
        }
    }

    @Test
    fun part2() {
        var carts = this.carts.toList()
        while (true) {
            val locations = mutableMapOf<Point, MutableSet<Cart>>()
            for (cart in carts.sortedBy { it.location }) {
                locations.computeIfAbsent(cart.location) { mutableSetOf() }.add(cart)
                cart.move(grid)
                locations.computeIfAbsent(cart.location) { mutableSetOf() }.add(cart)
            }
            val crashes = locations.entries.filter { it.value.size >= 2 }.flatMap { it.value }.toSet()
            carts = locations.entries.filter { it.value.size < 2 }.flatMap { it.value }.toSet().minus(crashes).toList()
            if (carts.size < 2) {
                carts[0].let { println("${it.location.x},${it.location.y}") }
                break
            }
        }
    }
}
