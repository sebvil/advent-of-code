package y23

import utils.*

data object Day1 : Day<List<Day1.Input>> {
    data class Input(val digits: List<String>)

    override val day: Int = 1
    override val part1TestInput: String = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent()
    override val part2TestInput: String = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent()
    override val part1Expected: Number = 142
    override val part2Expected: Number = 281

    override fun parseFile(file: List<String>): List<Input> {
        val digits =
            (1..9).map { it.toString() } + Digits.entries.filter { it != Digits.Zero }.map { it.name.lowercase() }
        return file.map { line ->
            instanceFromRegex(
                text = line,
                regex = regex(Regex(pattern = "(.*)"))
                {
                    regex(Regex(pattern = "(?=(${digits.joinToString("|")}))")) forGroup 0
                })
        }
    }

    override fun part1(input: List<Input>): Number {
        return input.sumOf { row ->
            val digits = row.digits.filter { it[0].isDigit() }
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    override fun part2(input: List<Input>): Number {
        return input.sumOf { row ->
            val digits = row.digits.map {
                if (it[0].isDigit()) it else enumFromString<Digits>(it).ordinal
            }
            "${digits.first()}${digits.last()}".toInt()
        }
    }

}