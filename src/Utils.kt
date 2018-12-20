import kotlin.math.abs

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    val neighbours: List<Point>
        get() = listOf(Point(x, y - 1), Point(x - 1, y), Point(x + 1, y), Point(x, y + 1))
    val up by lazy { neighbours[0] }
    val left by lazy { neighbours[1] }
    val right by lazy { neighbours[2] }
    val down by lazy { neighbours[3] }

    fun walk(direction: Char): Point = when (direction) {
        'N' -> up
        'E' -> right
        'S' -> down
        'W' -> left
        else -> throw RuntimeException(direction.toString())
    }

    override fun compareTo(other: Point): Int = compareValuesBy(this, other, { it.y }, { it.x })
    fun manhattan(other: Point): Int = abs(x - other.x) + abs(y - other.y)
    fun heuristicCost(other: Point): Int {
        val heightToGo = abs(y - other.y)
        val widthToGo = abs(x - other.x)
        val minToGo = minOf(heightToGo, widthToGo)
        val maxToGo = maxOf(heightToGo, widthToGo)
        return minToGo * 3 + (maxToGo - minToGo) * 2
    }
}

class Path(val map: Array<CharArray>,
           private val start: Point,
           private val goal: Point) {
    tailrec fun getShortestPath(ways: List<AStarElement> = listOf(aStarElement(listOf(start), 0)),
                                walked: Set<Point> = emptySet()): List<Point>? {
        if (ways.isEmpty()) return null
        val element = ways[0]
        val tail = ways.drop(1)
        return when {
            element.wayEnd == goal -> element.way
            element.wayEnd in walked -> getShortestPath(tail, walked)
            else -> getShortestPath((tail + neighbors(element)).sorted(), walked + element.wayEnd)
        }
    }

    private fun aStarElement(way: List<Point>,
                             realCost: Int): AStarElement = AStarElement(way, realCost, goal.heuristicCost(way.last()))

    private fun neighbors(element: AStarElement): List<AStarElement> = neighborsAndCost(element.wayEnd).map { (point, cost) -> aStarElement(element.way + point, element.realCost + cost) }
    private fun neighborsAndCost(point: Point): List<Pair<Point, Int>> = listOf(Point(point.x, point.y - 1) to 2, Point(point.x - 1, point.y) to 2, Point(point.x + 1, point.y) to 2, Point(point.x, point.y + 1) to 2).filter { isValid(it.first) }
    private fun isValid(point: Point): Boolean = point.x in 0 until map[0].size - 1 && point.y in 0 until map.size && map[point.y][point.x] == ' '
}

data class AStarElement(val way: List<Point>,
                        val realCost: Int,
                        private val heuristicCost: Int) : Comparable<AStarElement> {
    private val cost get() = realCost + heuristicCost
    val wayEnd: Point get() = way.last()
    override fun compareTo(other: AStarElement): Int = cost - other.cost
}
