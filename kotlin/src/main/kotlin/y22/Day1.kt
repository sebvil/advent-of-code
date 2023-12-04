package y22

import utils.*

data object Day1 : Day<List<Day1.Input>> {

    data class Input(val calories: List<Int>) // TODO()

    override val day: Int = 1
    override val part1TestInput: String = """
        1000
        2000
        3000
        
        4000
        
        5000
        6000
        
        7000
        8000
        9000
        
        10000
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 24000 // TODO
    override val part2Expected: Number = 45000 // TODO

    override fun parseFile(file: List<String>): List<Input> {
        return instancesFromRegex(Input::class, text = file.joinToString(), regex = regex(Regex("(.*?, ,| .+)")) {
            regex(Regex("([0-9]+)")) forGroup 0
        })
    }

    override fun part1(input: List<Input>): Number {
        return input.maxOfOrNull { it.calories.sum() }!!
    }

    override fun part2(input: List<Input>): Number {
        return input.map { it.calories.sum() }.sortedDescending().subList(0, 3).sum()
    }

}