package days

@Suppress("unused")
object D16 : Day {
    private val grid = readFile()

    private fun next(x: Int, y: Int, dir: NESW): List<NESW> =
        when (val c = grid[x][y]) {
            '.' -> listOf(dir)
            '-' -> if (dir == NESW.EAST || dir == NESW.WEST) listOf(dir) else listOf(NESW.EAST, NESW.WEST)
            '|' -> if (dir == NESW.NORTH || dir == NESW.SOUTH) listOf(dir) else listOf(NESW.NORTH, NESW.SOUTH)
            '/' -> when (dir) {
                NESW.NORTH -> listOf(NESW.EAST)
                NESW.SOUTH -> listOf(NESW.WEST)
                NESW.EAST -> listOf(NESW.NORTH)
                NESW.WEST -> listOf(NESW.SOUTH)
            }
            '\\' -> when (dir) {
                NESW.NORTH -> listOf(NESW.WEST)
                NESW.SOUTH -> listOf(NESW.EAST)
                NESW.EAST -> listOf(NESW.SOUTH)
                NESW.WEST -> listOf(NESW.NORTH)
            }
            else -> error("Unexpected token $c")
        }

    private fun getEnergizedCells(start: Triple<Int, Int, NESW>): Int {
        val boolGrid = Array(grid.size) { BooleanArray(grid[0].length) }
        val seen = mutableSetOf<Triple<Int, Int, NESW>>()
        var next = listOf(start)
        while (next.isNotEmpty()) {
            next = next.mapNotNull { triple ->
                val (x, y, dir) = triple
                if (x !in grid.indices || y !in grid[0].indices || triple in seen)  return@mapNotNull null
                boolGrid[x][y] = true
                seen.add(triple)
                next(x, y, dir).map { nextDir ->
                    val (xo, yo) = nextDir.offset
                    Triple(x + xo, y + yo, nextDir)
                }
            }.flatten()
        }
        return boolGrid.sumOf { row -> row.count { b -> b } }
    }

    override suspend fun part1(): String =
        getEnergizedCells(Triple(0, 0, NESW.EAST)).toString()

    override suspend fun part2(): String {
        val northMax = grid[0].indices.maxOf { y -> getEnergizedCells(Triple(grid.lastIndex, y, NESW.NORTH)) }
        val southMax = grid[0].indices.maxOf { y -> getEnergizedCells(Triple(0, y, NESW.SOUTH)) }
        val eastMax = grid.indices.maxOf { x -> getEnergizedCells(Triple(x, 0 , NESW.EAST)) }
        val westMax = grid.indices.maxOf { x -> getEnergizedCells(Triple(x, grid[0].lastIndex , NESW.WEST)) }
        return northMax.coerceAtLeast(southMax).coerceAtLeast(eastMax).coerceAtLeast(westMax).toString()
    }

    private enum class NESW(val offset: Pair<Int, Int>) {
        NORTH(-1 to 0), SOUTH(1 to 0), EAST(0 to 1), WEST(0 to -1)
    }
}
