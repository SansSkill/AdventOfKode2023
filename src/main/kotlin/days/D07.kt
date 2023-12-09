package days

@Suppress("unused")
object D07 : Day {
    private fun cardToInt(card: Char, jIsJoker: Boolean): Int =
        when (card) {
            'A' -> 12
            'K' -> 11
            'Q' -> 10
            'J' -> if (jIsJoker) 0 else 9
            'T' -> if (jIsJoker) 9 else 8
            else -> card - (if (jIsJoker) '1' else '2')
        }

    private fun getRank(cards: String, jIsJoker: Boolean): Int {
        val rawCounts = cards.groupingBy { it }.eachCount()
        val jokers = rawCounts['J'] ?: 0
        val counts = buildList<Int> {
            addAll(rawCounts.values)
            sortDescending()
            if (jIsJoker && jokers != 5) {
                remove(jokers)
                this[0] += jokers
            }
        }
        return when {
            counts[0] == 5 -> 6
            counts[0] == 4 -> 5
            counts[0] == 3 && counts[1] == 2 -> 4
            counts[0] == 3 -> 3
            counts[0] == 2 && counts[1] == 2 -> 2
            counts[0] == 2 -> 1
            else -> 0
        }
    }

    private fun scoreHands(jIsJoker: Boolean): Int {
        val hands = readFile().map { line ->
            val (cards, points) = line.split(" ")
            Hand(getRank(cards, jIsJoker), cards, points.toInt())
        }
        val comparator = Comparator<Hand> { (rank1, cards1), (rank2, cards2) ->
            if (rank1 > rank2) return@Comparator 1
            else if (rank1 < rank2) return@Comparator -1

            for (i in cards1.indices) {
                if (cards1[i] == cards2[i]) continue
                return@Comparator cardToInt(cards1[i], jIsJoker) - cardToInt(cards2[i], jIsJoker)
            }
            error("Hands are equal")
        }
        return hands.sortedWith(comparator).mapIndexed { index, hand -> index.inc() * hand.points }.sum()
    }

    override suspend fun part1(): String =
        scoreHands(false).toString()

    override suspend fun part2(): String =
        scoreHands(true).toString()

    private data class Hand(
        val rank: Int,
        val cards: String,
        val points: Int,
    )
}
