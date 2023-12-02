package days

interface Day {
    fun part1(): String
    fun part2(): String
    fun readFile(): List<String> = this::class.java.getResource("${this::class.simpleName}.txt").readText().split("\n")
}
