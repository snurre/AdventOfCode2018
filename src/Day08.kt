import org.junit.jupiter.api.Test
import java.io.File

class Day08 {
    class Node {
        val children: MutableList<Node> = mutableListOf()
        val metadata: MutableList<Int> = mutableListOf()
    }

    private val values = File(this.javaClass.getResource("08.txt").path).readText().trim().split(' ').map { it.toInt() }
    private val root = getNode(0).second

    private fun getNode(i: Int): Pair<Int, Node> {
        val n = Node()
        var j = i + 2
        for (k in 0 until values[i]) {
            val r = getNode(j)
            j = r.first
            n.children.add(r.second)
        }
        for (k in 0 until values[i + 1]) n.metadata.add(values[j++])
        return j to n
    }

    private fun sumMetadata(n: Node): Int = n.metadata.sum() + n.children.sumBy { sumMetadata(it) }

    private fun sumMetadataByReference(n: Node): Int =
            if (n.children.isEmpty()) n.metadata.sum() else n.metadata.sumBy { if (it > 0 && it <= n.children.size) sumMetadataByReference(n.children[it - 1]) else 0 }

    @Test
    fun part1() {
        println(sumMetadata(root))
    }

    @Test
    fun part2() {
        println(sumMetadataByReference(root))
    }

}
