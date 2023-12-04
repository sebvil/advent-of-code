package y23

import utils.*
import kotlin.math.pow

data object Day4 : Day<List<Day4.Input>> {
    data class Input(val winning: List<Int>, val card: List<Int>)

    override val day: Int = 4
    override val part1TestInput: String = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 13
    override val part2Expected: Number = 30

    override fun parseFile(file: List<String>): List<Input> {
        return file.map { line ->
            instanceFromRegex(
                text = line,
                pattern = "Card\\s*[0-9]+: (.*) \\| (.*)"
            ) {
                regex("([0-9]+)") forGroup 0
                regex("([0-9]+)") forGroup 1
            }
        }
    }

    override fun part1(input: List<Input>): Number {
        return input.sumOf {
            val size = it.card.filter { num -> num in it.winning }.size
            if (size == 0) 0 else 2.0.pow(size - 1).toInt()
        }
    }

    override fun part2(input: List<Input>): Number {
        val counts = mutableMapOf<Int, Int>()
        input.indices.forEach { counts[it] = 1 }
        input.forEachIndexed { index, it ->
            val wins = it.card.filter { num -> num in it.winning }.size
            val multiplier = counts[index] ?: 1
            ((index + 1)..(index + wins)).forEach { idx ->
                counts[idx] = (counts[idx] ?: 1) + multiplier
            }
        }
        return counts.alsoPrint().values.sum()
    }

}