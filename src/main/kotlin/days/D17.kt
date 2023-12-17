package days

import java.util.TreeSet

@Suppress("unused")
object D17 : Day {
    private val grid = readFile().map { line -> line.map(Char::digitToInt)}

    override suspend fun part1(): String =
        calculateHeatLoss(1, 3).toString()

    override suspend fun part2(): String =
        calculateHeatLoss(4, 10).toString()

    private fun calculateHeatLoss(min: Int, max: Int): Int {
        val treeSet = TreeSet(compareBy(State::heatLoss).thenBy(State::x).thenBy(State::y).thenBy(State::isHor))
        val seen = mutableSetOf<Triple<Int, Int, Boolean>>()
        treeSet.add(State(0, 0, 0, true))
        treeSet.add(State(0, 0, 0, false))
        while (treeSet.isNotEmpty()) {
            val state = treeSet.first().also(treeSet::remove)
            if (state.x == grid.lastIndex && state.y == grid[0].lastIndex) return state.heatLoss
            val triple = Triple(state.x, state.y, state.isHor)
            if (triple in seen) continue else seen.add(triple)
            treeSet.addAll(state.next(min, max))
        }
        error("TreeSet is empty")
    }

    private fun State.next(min: Int, max: Int): List<State> =
        buildList {
            val xo = if (isHor) 0 else 1
            val yo = if (isHor) 1 else 0
            var state = this@next.copy(isHor = !isHor)
            fun process(x: Int, y: Int): Boolean {
                if (x !in grid.indices || y !in grid[0].indices) return false
                state = state.copy(x = x, y = y, heatLoss = state.heatLoss + grid[x][y])
                return true
            }
            for (i in 1..max) {
                if (!process(state.x + xo, state.y + yo)) break
                if (i >= min) add(state)
            }
            state = this@next.copy(isHor = !isHor)
            for (i in 1..max) {
                if (!process(state.x - xo, state.y - yo)) break
                if (i >= min) add(state)
            }
        }

    private data class State(
        val heatLoss: Int,
        val x: Int,
        val y: Int,
        val isHor: Boolean
    )
}