import utils.*

data object Day3 : Day<List<Day3.Input>> {
    data class Input(val foo: String) // TODO()

    override val day: Int = 3
    override val part1TestInput: String = """
        TODO
    """.trimIndent()
    override val part2TestInput: String = """
        TODO
    """.trimIndent()
    override val part1Expected: Number = 0 // TODO
    override val part2Expected: Number = 0 // TODO

    override fun parseFile(file: List<String>): List<Input> {

        return file.map { line ->
            instanceFromRegex(
                text = line,
                regex = regex(Regex(pattern = "(.*)"))
                {
                    // TODO
                })
        }
    }

    override fun part1(input: List<Input>): Number {
        return 1
    }

    override fun part2(input: List<Input>): Number {
        return 1
    }

}