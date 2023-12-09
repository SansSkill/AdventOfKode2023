package days

@Suppress("unused")
object D08 : Day {
    private val instructions: String
    private val map: Map<String, Pair<String, String>>

    init {
        val file = readFile()
        instructions = file[0]
        map = file.drop(2).associate { line -> line.take(3) to (line.substring(7, 10) to line.substring(12, 15)) }
    }

    private fun getPath(start: String, isEnd: (String) -> Boolean): Int {
        var count = 0
        var location = start
        do {
            val (l, r) = map[location] ?: error("Dead end")
            location = if (instructions[count++ % instructions.length] == 'L') l else r
        } while (!isEnd(location))
        return count
    }

    override suspend fun part1(): String =
        getPath("AAA") { s -> s == "ZZZ" }.toString()

    override suspend fun part2(): String =
        map.keys.filter { location -> location.last() == 'A' }.map { start ->
            getPath(start) { location -> location.endsWith('Z') }.toBigInteger()
        }.reduce { acc, bigInteger -> acc * bigInteger / acc.gcd(bigInteger) }.toString()
}
