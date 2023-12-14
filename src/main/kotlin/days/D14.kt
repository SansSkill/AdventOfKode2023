package days

@Suppress("unused")
object D14 : Day {
    private const val EMPTY = 0
    private const val ROCK = 1
    private const val CUBE = 2
    private val grid: List<IntArray> = readFile().map { line ->
        IntArray(line.length) { i ->
            when (line[i]) {
                '.' -> EMPTY
                'O' -> ROCK
                '#' -> CUBE
                else -> error("Unexpected token ${line[i]}")
            }
        }
    }

    private fun rollNorth() {
        for (max in grid.size downTo 1) for (i in 1 ..< max) for (j in grid[0].indices) {
            if (grid[i][j] == ROCK && grid[i - 1][j] == EMPTY) {
                grid[i][j] = EMPTY
                grid[i - 1][j] = ROCK
            }
        }
    }

    private fun rollSouth() {
        for (min in 0 .. grid.size - 2) for (i in grid.size - 2 downTo min) for (j in grid[0].indices) {
            if (grid[i][j] == ROCK && grid[i + 1][j] == EMPTY) {
                grid[i][j] = EMPTY
                grid[i + 1][j] = ROCK
            }
        }
    }

    private fun rollEast() {
        for (min in 0 .. grid[0].size) for (j in grid[0].size - 2 downTo min) for (i in grid.indices) {
            if (grid[i][j] == ROCK && grid[i][j + 1] == EMPTY) {
                grid[i][j] = EMPTY
                grid[i][j + 1] = ROCK
            }
        }
    }

    private fun rollWest() {
        for (max in grid[0].size downTo 1) for (j in 1 ..< max) for (i in grid.indices) {
            if (grid[i][j] == ROCK && grid[i][j - 1] == EMPTY) {
                grid[i][j] = EMPTY
                grid[i][j - 1] = ROCK
            }
        }
    }

    private fun getLoad(grid: List<IntArray>): Int =
        grid.indices.sumOf { x -> (grid.size - x) * grid[x].count { n -> n == ROCK } }

    override suspend fun part1(): String {
        rollNorth()
        return getLoad(grid).toString()
    }

    override suspend fun part2(): String {
        rollWest()
        rollSouth()
        rollEast()
        val oldGrids = mutableListOf(grid.map(IntArray::copyOf))
        for (cycle in 1 ..< 999999999) {
            rollNorth()
            rollWest()
            rollSouth()
            rollEast()
            val i = oldGrids.indexOfFirst { oldGrid -> grid.indices.all { i -> grid[i].contentEquals(oldGrid[i]) } }
            if (i == -1) { oldGrids.add(grid.map(IntArray::copyOf)); continue }
            val cycleLength = cycle - i
            val remaining = 999999999 - cycle
            val offset = remaining % cycleLength
            return getLoad(oldGrids[i + offset]).toString()
        }
        return getLoad(grid).toString()
    }
}
