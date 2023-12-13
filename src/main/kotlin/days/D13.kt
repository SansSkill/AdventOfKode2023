package days

@Suppress("unused")
object D13 : Day {
    private val patterns: List<List<String>> = buildList {
        val file = readFile()
        var offset = 0
        while (offset < file.size) {
            add(file.drop(offset).takeWhile(String::isNotBlank))
            offset += last().size.inc()
        }
    }

    private fun findReflection(max: Int, count: Int, fn: (Int, Int) -> (Int)): Int? =
        (0 ..< max).find { i ->
            (0.. i.coerceAtMost(max - i - 1))
                .sumOf { offset -> fn(i - offset, i + 1 + offset) } == count
        }?.inc()

    private fun findHorizontalReflection(pattern: List<String>, count: Int): Int? =
        findReflection(pattern.lastIndex, count) { i, j -> pattern[i].indices.count { y -> pattern[i][y] != pattern[j][y] } }

    private fun findVerticalReflection(pattern: List<String>, count: Int): Int? =
        findReflection(pattern[0].lastIndex, count) { i, j -> pattern.indices.count { x -> pattern[x][i] != pattern[x][j] } }

    private fun scoreReflectionLines(count: Int): Int =
        patterns.sumOf { pattern ->
            findHorizontalReflection(pattern, count)?.let { n -> 100 * n }
                ?: findVerticalReflection(pattern, count)
                ?: error("No reflection found")
        }

    override suspend fun part1(): String =
        scoreReflectionLines(0).toString()

    override suspend fun part2(): String =
        scoreReflectionLines(1).toString()
}
