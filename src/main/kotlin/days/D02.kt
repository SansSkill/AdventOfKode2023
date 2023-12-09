package days

@Suppress("unused")
object D02 : Day {
    private val games: List<List<Triple<Int, Int, Int>>>
    init {
        val redRegex = Regex("(\\d+) red")
        val greenRegex = Regex("(\\d+) green")
        val blueRegex = Regex("(\\d+) blue")
        games = readFile().map { line ->
            line.split(";").map { set ->
                val red = redRegex.find(set)?.groupValues?.get(1)?.toInt() ?: 0
                val green = greenRegex.find(set)?.groupValues?.get(1)?.toInt() ?: 0
                val blue = blueRegex.find(set)?.groupValues?.get(1)?.toInt() ?: 0
                Triple(red, green, blue)
            }
        }
    }

    override suspend fun part1(): String =
        games.indices.filter { i ->
            games[i].all { (r, g, b) -> r <= 12 && g <= 13 && b <= 14 }
        }.sumOf(Int::inc).toString()

    override suspend fun part2(): String =
        games.sumOf { sets ->
            sets.reduce { (r1, g1, b1), (r2, g2, b2) -> Triple(r1.coerceAtLeast(r2), g1.coerceAtLeast(g2), b1.coerceAtLeast(b2)) }.let { (r, g, b) -> r * g * b }
        }.toString()
}
