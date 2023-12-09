package days

@Suppress("unused")
object D09 : Day {
    private val lines = readFile().map { line -> line.split(" ").map(String::toInt) }

    private fun getHistory(sequence: List<Int>): List<List<Int>> =
        buildList {
            add(sequence)
            while (last().any { n -> n != 0 }) {
                last().let { prev -> add((1..< prev.size).map { i -> prev[i] - prev[i - 1] }) }
            }
            reverse()
        }

    override suspend fun part1(): String =
        lines.sumOf { line -> getHistory(line).fold(0L) { acc, ints -> acc + ints.last() } }.toString()

    override suspend fun part2(): String =
        lines.sumOf { line -> getHistory(line).fold(0L) { acc, ints -> ints[0] - acc } }.toString()
}
