package days

import kotlin.math.abs

@Suppress("unused")
object D18 : Day {
    private val input = readFile().map { s ->
        val (dir, n, hash) = s.split(" ")
        Triple(dir[0], n.toInt(), hash)
    }

    private fun applyShoelace(transform: (Triple<Char, Int, String>) -> Pair<Int, Int>): Long {
        val points = buildList {
            var x = 0L
            var y = 0L
            input.forEach { triple ->
                val (dx, dy) = transform(triple)
                x += dx
                y += dy
                add(x to y)
            }
        }
        return points.indices.sumOf { i ->
            val (x1, y1) = points[i]
            val (x2, y2) = points[i.inc() % points.size]
            x1 * y2 - x2 * y1 + abs(x1 - x2) + abs(y1 - y2)
        }.div(2).inc()
    }

    override suspend fun part1(): String =
        applyShoelace { (dir, n) ->
            when (dir) {
                'R' -> n to 0
                'L' -> -n to 0
                'D' -> 0 to n
                'U' -> 0 to -n
                else -> error("Unexpected dir $dir")
            }
        }.toString()

    override suspend fun part2(): String =
        applyShoelace { (_, _, hex) ->
            val n = hex.substring(2, 7).toInt(16)
            when (val dir = hex[7]) {
                '0' /* R */ -> n to 0
                '2' /* L */ -> -n to 0
                '1' /* D */-> 0 to n
                '3' /* U */ -> 0 to -n
                else -> error("Unexpected dir $dir")
            }
        }.toString()
}
