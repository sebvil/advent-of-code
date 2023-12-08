package y23

import utils.*

data object Day8 : Day<List<Day8.Input>> {

    enum class Instruction {
        L, R
    }

    data class Input(val current: String, val left: String, val right: String)


    override val day: Int = 8
    override val part1TestInput: String = """
        LLR

        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent()
    override val part2TestInput: String = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent()
    override val part1Expected: Number = 6 // TODO
    override val part2Expected: Number = 6L

    private lateinit var instructions: List<Instruction>

    override fun parseFile(file: List<String>): List<Input> {
        instructions = instancesFromRegex(Instruction::class, file.first(), regex("([RL])"))
        return file.drop(2).map { line ->
            instanceFromRegex(
                text = line,
                pattern = "(.+) = \\((.+), (.+)\\)"
            ) {
                regex("(.*)") forGroup 0
                regex("(.*)") forGroup 1
                regex("(.*)") forGroup 2
            }
        }
    }

    override fun part1(input: List<Input>): Number {
        val mapping = input.associate {
            it.current to Pair(it.left, it.right)
        }

        return pathToZ(starting = "AAA", mapping) { it == "ZZZ" }
    }

    private fun pathToZ(starting: String, mapping: Map<String, Pair<String, String>>, done: (String) -> Boolean): Int {
        var count = 0
        var current = starting
        while (!done(current)) {
            val instruction = instructions[count % instructions.size]
            current = when (instruction) {
                Instruction.L -> mapping[current]!!.first
                Instruction.R -> mapping[current]!!.second
            }
            count++
        }
        return count
    }

    override fun part2(input: List<Input>): Number {
        val mapping = input.associate {
            it.current to Pair(it.left, it.right)
        }
        val paths =
            input.filter { it.current.last() == 'A' }.map { pathToZ(it.current, mapping) { s -> s.last() == 'Z' } }

        return lcm(paths.toMutableList())
    }


}