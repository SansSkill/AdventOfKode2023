package days

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@Suppress("unused")
object D05 : Day {
    private val seeds: List<Long>
    private val seedRanges: List<LongRange>
    private val seedToSoilMap: List<Pair<LongRange, Long>>
    private val soilToFertilizerMap: List<Pair<LongRange, Long>>
    private val fertilizerToWaterMap: List<Pair<LongRange, Long>>
    private val waterToLightMap: List<Pair<LongRange, Long>>
    private val lightToTemperatureMap: List<Pair<LongRange, Long>>
    private val temperatureToHumidityMap: List<Pair<LongRange, Long>>
    private val humidityToLocationMap: List<Pair<LongRange, Long>>

    init {
        val input = readFile()
        fun toPair(line: String): Pair<LongRange, Long> = line.split(" ").map(String::toLong).let { (n1, n2, n3) -> n2 ..< n2 + n3 to n1 }
        seeds = Regex("\\d+").findAll(input[0]).map(MatchResult::value).map(String::toLong).toList()
        seedRanges = (seeds.indices step 2).map { i -> seeds[i]..<seeds[i] + seeds[i + 1] }
        var dropOffset = 3
        fun getMap() = input.drop(dropOffset).takeWhile(String::isNotEmpty).map(::toPair).sortedBy { (range, _) -> range.first }.also { map -> dropOffset += map.size + 2 }
        seedToSoilMap = getMap()
        soilToFertilizerMap = getMap()
        fertilizerToWaterMap = getMap()
        waterToLightMap = getMap()
        lightToTemperatureMap = getMap()
        temperatureToHumidityMap = getMap()
        humidityToLocationMap = getMap()
    }

    private fun getLocation(seed: Long): Long =
        listOf(seedToSoilMap, soilToFertilizerMap, fertilizerToWaterMap, waterToLightMap, lightToTemperatureMap, temperatureToHumidityMap, humidityToLocationMap)
            .fold(seed) { pos, map -> map.firstOrNull { (range) -> pos in range }?.let { (range, m) -> m + pos - range.first } ?: pos }

    override suspend fun part1(): String =
        seeds.minOf(::getLocation).toString()

    override suspend fun part2(): String = withContext(Dispatchers.IO) {
        seedRanges.map { range -> async { range.minOf(::getLocation) } }.awaitAll().min().toString()
    }
}
