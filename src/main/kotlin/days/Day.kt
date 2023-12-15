package days

interface Day {
    suspend fun part1(): String
    suspend fun part2(): String
}

fun Day.readFile(): List<String> = this::class.java.getResource("${this::class.simpleName}.txt")!!.readText().split("\r\n")
