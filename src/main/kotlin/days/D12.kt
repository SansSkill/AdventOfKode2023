package days

@Suppress("unused")
object D12 : Day {
    private val input = readFile().map { line -> line.split(" ").let { (l, r) -> l to r } }

    private fun getArrangements(s: String, ls: List<Int>): Long {
        val memo = IntArray(s.length) { i -> s.drop(i).takeWhile { c -> c != '.' }.length }
        val dp = mutableMapOf<Pair<Int, Int>, Long>()

        fun canTake(i: Int, l: Int) = memo[i] >= l && (i + l == s.length || s[i + l] != '#')
        fun helper(si: Int, lsi: Int): Long =
            when {
                lsi == ls.size -> if (s.drop(si).none { c -> c == '#' }) 1L else 0
                si >= s.length -> 0L
                else -> {
                    if (dp[si to lsi] == null) {
                        val take = if (canTake(si, ls[lsi])) helper(si + ls[lsi] + 1, lsi + 1) else 0L
                        val dontTake = if (s[si] != '#') helper(si + 1, lsi) else 0L
                        dp[si to lsi] = take + dontTake
                    }
                    dp[si to lsi]!!
                }
            }
        return helper(0, 0)
    }

    override suspend fun part1(): String =
        input.sumOf { (s, counts) ->
            getArrangements(s, counts.split(",").map(String::toInt))
        }.toString()

    override suspend fun part2(): String =
        input.sumOf { (s, counts) ->
            getArrangements("$s?$s?$s?$s?$s", "$counts,$counts,$counts,$counts,$counts".split(",").map(String::toInt))
        }.toString()

}
