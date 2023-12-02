import utils.readInput

sealed class Day<Input> {
    abstract val day: Int
    private val filename: String = ""

    val input: Input
        get() = parseFile(readInput(day))

    abstract fun parseFile(file: List<String>): Input
    abstract fun part1(): Number
    abstract fun part2(): Number
}