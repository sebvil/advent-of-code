import utils.*

data object Day1 : Day<List<Day1.Input>>() {

    override val day: Int = 1
    data class Input(val digits: List<String>)
    override fun parseFile(file: List<String>): List<Input> {
        val digits =
            (1..9).map { it.toString() } + Digits.entries.filter { it != Digits.Zero }.map { it.name.lowercase() }
        return file.map { line ->
            instanceFromRegex<Input>(
                text = line,
                regex = regex(Regex(pattern = "(.*)"))
                {
                    regex(Regex(pattern = "(?=(${digits.joinToString("|")}))")) forGroup 0
                })
        }
    }

    override fun part1(): Number {
        return input.sumOf { row ->
            val digits = row.digits.filter { it[0].isDigit() }
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    override fun part2(): Number {
        return input.sumOf { row ->
            val digits = row.digits.map {
                if (it[0].isDigit()) it else enumFromString<Digits>(it).ordinal
            }
            "${digits.first()}${digits.last()}".toInt()
        }
    }

}