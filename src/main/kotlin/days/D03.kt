package days

@Suppress("unused")
object D03 : Day {
    private val numberRegex = Regex("\\d+")
    private val grid = readFile()
    private val numberRanges = grid.flatMapIndexed { idx, line -> numberRegex.findAll(line).map { matchResult -> idx to matchResult.range } }

    override suspend fun part1(): String {
        fun isSpecialCharacter(x: Int, y: Int): Boolean =
            if (x < 0 || x >= grid.size || y < 0 || y >= grid[0].length) false
            else grid[x][y] !in "0123456789."
        return numberRanges.filter { (idx, range) ->
            (range.first.dec()..range.last.inc()).any { y -> isSpecialCharacter(idx - 1, y) || isSpecialCharacter(idx + 1, y) } || isSpecialCharacter(idx, range.first - 1) || isSpecialCharacter(idx, range.last + 1)
        }.sumOf { (idx, range) -> grid[idx].substring(range).toInt() }.toString()
    }

    override suspend fun part2(): String {
        fun isSpecialCharacter(x: Int, y: Int): Boolean =
            if (x < 0 || x >= grid.size || y < 0 || y >= grid[0].length) false else grid[x][y] == '*'
        return buildMap<Pair<Int, Int>, MutableList<Pair<Int, IntRange>>> {
            fun handle(x: Int, y: Int, rangePair: Pair<Int, IntRange>) {
                if (isSpecialCharacter(x, y)) put(x to y, getOrDefault(x to y, mutableListOf()).apply { add(rangePair) })
            }
            numberRanges.forEach { (idx, range) ->
                (range.first.dec()..range.last.inc()).forEach { y ->
                    handle(idx - 1, y, idx to range)
                    handle(idx + 1, y, idx to range)
                }
                handle(idx, range.first - 1, idx to range)
                handle(idx, range.last + 1, idx to range)
            }
        }.values.filter { list -> list.size == 2 }.sumOf { list ->
            val (idx1, range1) = list[0]
            val (idx2, range2) = list[1]
            grid[idx1].substring(range1).toInt() * grid[idx2].substring(range2).toInt()
        }.toString()
    }
}
