package days

@Suppress("unused")
object D06 : Day {
    private val timeMatches: List<String>
    private val distanceMatches: List<String>

    init {
        val numberRegex = Regex("\\d+")
        val file = readFile()
        timeMatches = numberRegex.findAll(file[0]).map(MatchResult::value).toList()
        distanceMatches = numberRegex.findAll(file[1]).map(MatchResult::value).toList()
    }

    private fun getErrorMargin(time: Long, distance: Long): Long =
        (1..time).first { n -> n * (time - n) > distance }.let { n -> time - 2 * n + 1 }

    override suspend fun part1(): String =
        timeMatches.indices.map { i ->
            getErrorMargin(time = timeMatches[i].toLong(), distance = distanceMatches[i].toLong())
        }.reduce(Long::times).toString()

    override suspend fun part2(): String =
        getErrorMargin(
            time = timeMatches.joinToString("").toLong(),
            distance = distanceMatches.joinToString("").toLong(),
        ).toString()
}
