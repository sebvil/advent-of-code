package y23

import utils.*

data object Day3 : Day<List<Day3.Input>> {

    sealed interface Cell {
        @JvmInline
        value class Number(val value: Char) : Cell

        data object Period : Cell
        sealed interface Symbol : Cell

        data object NonGear : Symbol

        data object Gear : Symbol

        fun asString(): String {
            return when (this) {
                is Number -> value.toString()
                Period -> "."
                is Symbol -> "*"
            }
        }

        companion object {
            fun fromChar(char: Char): Cell {
                return when {
                    char.isDigit() -> Number(char)
                    char == '.' -> Period
                    char == '*' -> Gear
                    else -> NonGear
                }
            }
        }

    }

    data class Input(val row: List<Cell>)

    override val day: Int = 3
    override val part1TestInput: String = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 4361
    override val part2Expected: Number = 467835

    override fun parseFile(file: List<String>): List<Input> {
        return file.map { line ->
            Input(row = line.map { Cell.fromChar(it) })
        }
    }

    override fun part1(input: List<Input>): Number {
        return findNumbers(input).sum()
    }

    override fun part2(input: List<Input>): Number {
        return findGearRatios(input).sum()
    }

    private fun findValidPartNumbersIndices(input: List<Input>): List<Pair<Int, Int>> {
        val visitedNumbers = mutableMapOf<Pair<Int, Int>, Boolean>()
        fun isValidNumber(row: Int, column: Int): Boolean {
            if (row !in input.indices || column !in input[0].row.indices) {
                return false
            }
            val cell = input[row].row[column]
            if (cell !is Cell.Number) {
                return false
            }

            getAdjacentCells(input.map { it.row }, row, column).forEach { (r, c) ->
                if (r == row && c == column) {
                    return@forEach
                }
                when (input[r].row[c]) {
                    is Cell.Number -> {
                        val isMaybeValid = visitedNumbers[Pair(r, c)]
                        when (isMaybeValid) {
                            true -> {
                                visitedNumbers[Pair(row, column)] = true
                                return true
                            }

                            false -> Unit
                            null -> {
                                visitedNumbers[Pair(r, c)] = false
                                val isNextValid = isValidNumber(r, c)
                                if (isNextValid) {
                                    visitedNumbers[Pair(row, column)] = true
                                    return true
                                }
                            }
                        }

                    }

                    Cell.Period -> Unit
                    is Cell.Symbol -> {
                        visitedNumbers[Pair(row, column)] = true
                        return true
                    }
                }
            }

            return false
        }
        return input.flatMapIndexed { r, row ->
            row.row.mapIndexedNotNull { c, _ ->
                if (isValidNumber(r, c)) Pair(r, c) else null
            }
        }
    }

    private fun findNumbers(input: List<Input>): List<Int> {
        val validIndices = findValidPartNumbersIndices(input)
        val rowsWithValidNumbers = input.mapIndexed { rowIdx, row ->
            row.row.filterIndexed { col, cell -> Pair(rowIdx, col) in validIndices || cell !is Cell.Number }
        }.map { it.joinToString("") { cell -> cell.asString() } }
        return rowsWithValidNumbers.flatMap {
            instancesFromRegex(Int::class, it, regex(Regex("([0-9]+)")))
        }
    }

    private fun findGearRatios(input: List<Input>): List<Int> {
        val validIndices = findValidPartNumbersIndices(input)
        val rowsWithValidNumbers = input.mapIndexed { rowIdx, row ->
            row.row.mapIndexed { col, cell -> if (Pair(rowIdx, col) in validIndices) cell else Cell.Period }
        }.map { it.joinToString("") { cell -> cell.asString() } }
        val numberRanges: List<Triple<Int, IntRange, Int>> = rowsWithValidNumbers.flatMapIndexed { index, s ->
            Regex("([0-9]+)").findAll(s).map { matchResult ->
                Triple(index, matchResult.range, matchResult.value.toInt())
            }.toList()
        }

        fun findGearRatio(row: Int, column: Int): Int {
            val foundNumbers = mutableSetOf<Triple<Int, IntRange, Int>>()
            getAdjacentCells(input.map { it.row }, row, column).forEach { (r, c) ->
                if (r == row && c == column) {
                    return@forEach
                }
                val number = numberRanges.find { r == it.first && c in it.second } ?: return@forEach
                if (number !in foundNumbers) {
                    foundNumbers.add(number)
                }

            }

            return when {
                foundNumbers.size == 2 -> foundNumbers.fold(1) { acc, number -> acc * number.third }
                else -> 0
            }
        }


        return input.flatMapIndexed { r, row ->
            row.row.mapIndexedNotNull { index, cell ->
                if (cell is Cell.Gear) {
                    findGearRatio(r, index)
                } else {
                    null
                }
            }
        }
    }

}