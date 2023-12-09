package y23

import utils.*

data object Day9 : Day<List<Day9.Input>> {
    data class Input(val seq: List<Int>) // TODO()

    override val day: Int = 9
    override val part1TestInput: String = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 114 // TODO
    override val part2Expected: Number = 2 // TODO

    override fun parseFile(file: List<String>): List<Input> {

        return file.map { line ->
            instanceFromRegex(
                text = line,
                pattern = "(.*)"
            ) {
                regex("([-\\d]+)") forGroup 0
            }
        }
    }

    fun prev(seq: List<Int>): Int {
        val diffs = mutableListOf(seq)
        var curr = seq
        while (curr.any { it != 0 }) {
            curr = curr.zipWithNext { a, b -> b - a }
            diffs.add(curr)
        }
        return diffs.foldRight(0) { it, acc ->
            it.first() - acc
        }
    }

    fun next(seq: List<Int>): Int {
        val diffs = mutableListOf(seq)
        var curr = seq
        while (curr.any { it != 0 }) {
            curr = curr.zipWithNext { a, b -> b - a }
            diffs.add(curr)
        }
        return diffs.sumOf { it.last() }
    }

    override fun part1(input: List<Input>): Number {
        return input.sumOf { next(it.seq) }
    }

    override fun part2(input: List<Input>): Number {
        return input.sumOf { prev(it.seq) }
    }

}