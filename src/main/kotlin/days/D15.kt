package days

@Suppress("unused")
object D15: Day {
    private val steps = readFile()[0].split(",")

    private fun hash(s: String): Int =
        s.fold(0) { acc, c -> acc.plus(c.code).times(17).mod(256) }

    override suspend fun part1(): String =
        steps.sumOf(::hash).toString()

    override suspend fun part2(): String {
        val boxes = Array(256) { mutableMapOf<String, Pair<Int, Int>>() }
        val pos = IntArray(256)
        steps.forEach { step ->
            if (step.last() == '-') step.dropLast(1).let { label -> boxes[hash(label)].remove(label) }
            else {
                val focalLength = step.last().digitToInt()
                val label = step.dropLast(2)
                val box = hash(label)
                boxes[box][label] = focalLength to (boxes[box][label]?.second ?: pos[box]++)
            }
        }
        return boxes.mapIndexed { boxIndex, box ->
            box.values.sortedBy(Pair<Int, Int>::second).mapIndexed { lensIndex, (focalLength, _) ->
                boxIndex.inc() * lensIndex.inc() * focalLength
            }.sum()
        }.sum().toString()
    }
}
