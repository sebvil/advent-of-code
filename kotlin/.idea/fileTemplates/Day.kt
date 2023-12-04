#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
#parse("File Header.java")
import utils.*

data object Day${DAY_NUM} : Day<List<Day${DAY_NUM}.Input>> {
    data class Input(val foo: String) // TODO()

    override val day: Int = ${DAY_NUM}
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
                pattern = "(.*)") {
                   // TODO
                }
        }
    }

    override fun part1(input: List<Input>): Number {
        return 1
    }

    override fun part2(input: List<Input>): Number {
        return 1 
    }

}