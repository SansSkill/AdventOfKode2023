package days

@Suppress("unused")
object D05 : Day {
    private val seeds: List<Long>
    private val seedRanges: List<LongRange>
    private val seedToLocationMapList: List<List<Pair<LongRange, Long>>>
    private val locationToSeedMapList: List<List<Pair<LongRange, Long>>>

    init {
        val input = readFile()
        seeds = Regex("\\d+").findAll(input[0]).map(MatchResult::value).map(String::toLong).toList()
        seedRanges = (seeds.indices step 2).map { i -> seeds[i]..<seeds[i] + seeds[i + 1] }
        fun getMapList(toPair: (String) -> (Pair<LongRange, Long>)): List<List<Pair<LongRange, Long>>> {
            var dropOffset = 3
            return (0..6).map {
                input.drop(dropOffset).takeWhile(String::isNotEmpty)
                    .map { line -> toPair(line) }.sortedBy { (range, _) -> range.first }
                    .also { map -> dropOffset += map.size + 2 }
            }
        }
        seedToLocationMapList = getMapList { line ->
            line.split(" ").map(String::toLong).let { (n1, n2, n3) -> n2 ..< n2 + n3 to n1 }
        }
        locationToSeedMapList = getMapList { line ->
            line.split(" ").map(String::toLong).let { (n1, n2, n3) -> n1 ..< n1 + n3 to n2 }
        }.reversed()
    }

    private fun List<List<Pair<LongRange, Long>>>.fold(start: Long): Long =
        fold(start) { pos, map -> map.firstOrNull { (range) -> pos in range }?.let { (range, m) -> m + pos - range.first } ?: pos }

    override suspend fun part1(): String =
        seeds.minOf { seed -> seedToLocationMapList.fold(seed) }.toString()

    override suspend fun part2(): String =
        generateSequence(0L, Long::inc).first { location ->
            val seed = locationToSeedMapList.fold(location)
            seedRanges.any { range -> seed in range }
        }.toString()
}
