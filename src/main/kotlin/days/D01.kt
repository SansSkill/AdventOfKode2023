package days

@Suppress("unused")
object D01 : Day {
    private const val DIGIT_CHARS = "0123456789"
    override suspend fun part1(): String = readFile().sumOf { line ->
        val tenDigit = line[line.indexOfFirst { c -> c in DIGIT_CHARS }]
        val singleDigit = line[line.indexOfLast { c -> c in DIGIT_CHARS }]
        "$tenDigit$singleDigit".toInt()
    }.toString()

    private val digitMap = listOf(
        "0" to 0, "1" to 1, "2" to 2, "3" to 3,
        "4" to 4, "5" to 5, "6" to 6,
        "7" to 7, "8" to 8, "9" to 9,
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9,
    )
    override suspend fun part2(): String = readFile().sumOf { line ->
        val tenDigit = digitMap.minBy { (k, _) -> line.indexOf(k).takeIf { it >= 0 } ?: Int.MAX_VALUE }.second
        val singleDigit = digitMap.maxBy { (k, _) -> line.lastIndexOf(k) }.second
        "$tenDigit$singleDigit".toInt()
    }.toString()
}
