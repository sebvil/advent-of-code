import utils.readInput

interface BaseDay<Input> {
    private val input: Input
        get() = parseFile(readInput(year, day))

    val year: Int
    val day: Int
    val part1TestInput: String
    val part2TestInput: String
    val part1Expected: Number
    val part2Expected: Number
    fun parseFile(file: List<String>): Input

    fun part1(input: Input): Number
    fun part2(input: Input): Number

    fun checkPart1() {
        val result = part1(parseFile(part1TestInput.lines()))
        check(result == part1Expected) {
            "Part 1 failed. Got $result, expected: $part1Expected"
        }
        println("Part 1 test succeeded")
    }

    fun checkPart2() {
        val result = part2(parseFile(part2TestInput.lines()))
        check(result == part2Expected) {
            "Part 2 failed. Got $result, expected: $part2Expected"
        }
        println("Part 2 test succeeded")

    }

    fun part1(): Number = part1(input)
    fun part2(): Number = part2(input)
}