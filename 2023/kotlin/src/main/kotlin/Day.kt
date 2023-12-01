import utils.readInput

sealed class Day<Input> {
    abstract val day: Int
    abstract val part: Int
    private val filename: String = ""

    val input: Input
        get() = parseFile(readInput(day))

    abstract fun parseFile(file: List<String>): Input
    abstract fun solve(): Any
}