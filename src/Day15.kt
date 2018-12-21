import org.junit.jupiter.api.Test
import java.io.File

class Day15 {
    abstract class Fighter(var location: Point, val attackPower: Int = 3, var hp: Int = 300) {
        abstract fun copy(attackPower: Int = 3): Fighter
    }

    class Elf(location: Point, attackPower: Int = 3) : Fighter(location, attackPower) {
        override fun copy(attackPower: Int): Fighter = Elf(location, attackPower)
    }

    class Goblin(location: Point) : Fighter(location) {
        override fun copy(attackPower: Int): Fighter = Goblin(location)
    }

    private fun List<Fighter>.copy(elfPower: Int = 3) = this.map { it.copy(elfPower) }
    private fun List<Fighter>.living() = this.filter { it.hp > 0 }
    private fun Point.isFloor() = map[this.y][this.x] == ' '

    private val map: Array<CharArray>
    private val allFighters: List<Fighter>

    init {
        val lines = File("resources/15.txt").readLines()
        map = Array(lines.size) { CharArray(lines[0].length) { ' ' } }
        allFighters = mutableListOf()
        for (y in 0 until lines.size) {
            for (x in 0 until lines[y].length) {
                map[y][x] = if (lines[y][x] == '#') lines[y][x] else ' '
                if (lines[y][x] == 'G') allFighters.add(Goblin(Point(x, y)))
                else if (lines[y][x] == 'E') allFighters.add(Elf(Point(x, y)))
            }
        }
    }

    private fun dijkstra(from: Point, to: Point, occupiedSquares: Collection<Point>): List<Point>? {
        data class Distance(val distance: Int, val previous: Point? = null)

        val visited = mutableSetOf<Point>()
        val unvisited = mutableListOf<Point>()
        val distances = mutableMapOf<Point, Distance>()
        distances[from] = Distance(0)
        var current = from
        while (true) {
            unvisited += current.neighbours.filter { it.isFloor() && it !in occupiedSquares && it !in unvisited && it !in visited }
            for (pos in unvisited) {
                val distance = distances[current]!!.distance + 1
                if (distance < (distances[pos]?.distance ?: Int.MAX_VALUE)) distances[pos] = Distance(distance, current)
            }
            unvisited -= current
            visited += current
            if (current == to) {
                val route = mutableListOf(to)
                var distance = distances[to]!!
                while (distance.previous != null) {
                    route.add(distance.previous!!)
                    distance = distances[distance.previous!!]!!
                }
                route.reverse()
                return route.drop(1)
            }
            current = unvisited.sortedBy { distances[it]!!.distance }.firstOrNull() ?: break
        }
        return null
    }

    data class Result(val rounds: Int, val survivors: List<Fighter>)

    private fun fight(elfPower: Int): Result {
        val fighters = allFighters.copy(elfPower)
        var round = 0
        main@ while (true) {
            for (fighter in fighters.living().sortedBy { it.location }) {
                if (fighter.hp <= 0) continue
                val enemies = fighters.living().filter { (fighter is Goblin && it is Elf) || (fighter is Elf && it is Goblin) }
                if (enemies.isEmpty()) break@main
                var nearbyEnemies = enemies.filter { it.location in fighter.location.neighbours }
                if (nearbyEnemies.isEmpty()) {
                    val occupiedSquares = fighters.living().map { it.location }.toSet()
                    val nearest = enemies
                            .flatMap { it.location.neighbours }
                            .filter { it.isFloor() && it !in occupiedSquares }
                            .toSet()
                            .mapNotNull { dijkstra(fighter.location, it, occupiedSquares) }
                            .sortedWith(compareBy({ it.size }, { it.last() }))
                    if (nearest.isNotEmpty()) {
                        fighter.location = nearest.first().first()
                        nearbyEnemies = enemies.filter { it.location in fighter.location.neighbours }
                    }
                }
                if (nearbyEnemies.isNotEmpty()) {
                    nearbyEnemies.sortedWith(compareBy({ it.hp }, { it.location })).first().hp -= fighter.attackPower
                }
            }
            round++
        }
        return Result(round, fighters.living())
    }

    @Test
    fun part1() {
        val result = fight(3)
        println(result.rounds * result.survivors.sumBy { it.hp })
    }

    @Test
    fun part2() {
        var elfPower = 4
        while (true) {
            val result = fight(elfPower++)
            if (result.survivors.first() is Elf && result.survivors.size == allFighters.filter { it is Elf }.size) {
                println(result.rounds * result.survivors.sumBy { it.hp })
                break
            }
        }
    }
}
