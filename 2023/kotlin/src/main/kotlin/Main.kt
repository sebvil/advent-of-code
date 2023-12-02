import utils.alsoPrint
import utils.println

fun main() {
    val day = Day::class.sealedSubclasses
        .mapNotNull { it.objectInstance }
        .maxByOrNull { it.day  }!!
    day.part1().alsoPrint(pretty = false) { "Day ${day.day}, Part 1: $it" }
    day.part2().alsoPrint(pretty = false) { "Day ${day.day}, Part 2: $it" }
}