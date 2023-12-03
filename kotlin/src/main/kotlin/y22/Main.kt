package y22

import utils.alsoPrint

fun main() {
    val day = Day::class.sealedSubclasses
        .mapNotNull { it.objectInstance }
        .maxByOrNull { it.day  }!!
    day.checkPart1()
    day.part1().alsoPrint(pretty = false) { "Day ${day.day}, Part 1: $it" }
    day.checkPart2()
    day.part2().alsoPrint(pretty = false) { "Day ${day.day}, Part 2: $it" }
}