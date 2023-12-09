package days

@Suppress("unused")
object D04 : Day {
    private val numberRegex = Regex("\\d+")
    private val matches = readFile().map { line ->
        val numbers = numberRegex.findAll(line).map(MatchResult::value).map(String::toInt)
        val winningNumbers = numbers.drop(1).take(10)
        val yourNumbers = numbers.drop(11)
        winningNumbers.count { n -> n in yourNumbers }
    }

    private val scoreTable = listOf(0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512)
    override suspend fun part1(): String =
        matches.sumOf(scoreTable::get).toString()

    override suspend fun part2(): String {
        val dp = IntArray(matches.size).apply { fill(1) }
        matches.indices.asSequence().forEach { i ->
            (1..matches[i])
                .asSequence()
                .takeWhile { k -> i + k < dp.size }
                .forEach { k -> dp[i + k] += dp[i] }
        }
        return dp.sum().toString()
    }
}
