import days.D01
import days.D02

fun main() {
    val days = listOf(D01, D02)
    days.last().run {
        part1().let(::println)
        part2().let(::println)
    }
}
