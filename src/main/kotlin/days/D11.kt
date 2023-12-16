package days

@Suppress("unused")
object D11 : Day {
    private val galaxies: List<Pair<Int, Int>> = readFile().flatMapIndexed { x, line ->
        line.indices.filter { y -> line[y] == '#' }.map { y -> x to y }
    }
    private val emptyRows: List<Int> = galaxies.map(Pair<Int, Int>::first).toSet().let { set ->
        (set.min()..set.max()).filter { i -> i !in set }
    }
    private val emptyColumns: List<Int> = galaxies.map(Pair<Int, Int>::second).toSet().let { set ->
        (set.min()..set.max()).filter { i -> i !in set }
    }

    private fun getDistance(galaxy1: Pair<Int, Int>, galaxy2: Pair<Int, Int>, emptySize: Long): Long {
        val (x1, y1) = galaxy1
        val (x2, y2) = galaxy2
        val x = (x1.coerceAtMost(x2).inc()..x1.coerceAtLeast(x2)).sumOf { i ->
            if (i in emptyRows) emptySize else 1
        }
        val y = (y1.coerceAtMost(y2).inc()..y1.coerceAtLeast(y2)).sumOf { i ->
            if (i in emptyColumns) emptySize else 1
        }
        return x + y
    }

    private fun getTotalDistance(emptySize: Long): Long =
        (0 ..< galaxies.size - 1).sumOf { i ->
            (i.inc() ..< galaxies.size).sumOf { j ->
                getDistance(galaxies[i], galaxies[j], emptySize)
            }
        }

    override suspend fun part1(): String =
        getTotalDistance(2L).toString()

    override suspend fun part2(): String =
        getTotalDistance(1000000L).toString()
}
