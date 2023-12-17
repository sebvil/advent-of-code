package y23

import utils.*
import kotlin.math.abs

data object Day11 : Day<Day11.Input> {

    sealed class SpaceCell(override val stringValue: String) : ParsableEnum<SpaceCell> {

        data object Galaxy : SpaceCell("#")
        data object Space : SpaceCell(".")

        companion object {
            fun entries(): List<SpaceCell> = listOf(Galaxy, Space)
        }
    }


    data class Input(val grid: List<List<SpaceCell>>)

    override val day: Int = 11
    override val part1TestInput: String = """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent()
    override val part2TestInput: String = part1TestInput.trimIndent()
    override val part1Expected: Number = 374L
    override val part2Expected: Number = 82000210L

    override fun parseFile(file: List<String>): Input {
        return Input(
            grid = file.map { line ->
                instancesFromRegex(
                    clazz = SpaceCell::class,
                    text = line,
                    pattern = "([.#])"
                )
            }
        )

    }


    private fun Input.galaxies(): List<Cell> {
        return grid.flatMapIndexed { index: Int, spaceCells: List<SpaceCell> ->
            spaceCells.mapIndexedNotNull { col: Int, cell: SpaceCell ->
                if (cell is SpaceCell.Galaxy) {
                    Cell(index, col)
                } else {
                    null
                }
            }
        }
    }

    private fun List<Cell>.expand(ratio: Int = 2): List<LongCell> {
        val maxRow = maxOf { it.row }.toLong()
        val maxColumn = maxOf { it.column }.toLong()
        val emptyRows = (0..maxRow).toSet() - map { it.row.toLong() }.toSet()
        val emptyColumns = (0..maxColumn).toSet() - map { it.column .toLong()}.toSet()
        return map { (row, column) ->
            val newRow = row.toLong() + emptyRows.filter { it < row }.size * (ratio - 1)
            val newColumn = column.toLong() + emptyColumns.filter { it < column }.size * (ratio - 1)
            LongCell(newRow, newColumn)
        }
    }


    private fun List<LongCell>.pairs(): Set<Pair<LongCell, LongCell>> {
        return flatMap { c -> map { it to c } }.toSet()
    }

    private fun Pair<LongCell, LongCell>.distance() = abs(first.row - second.row) + abs(first.column - second.column)

    override fun part1(input: Input): Number {
        return  input.galaxies().expand().pairs().sumOf { it.distance() } / 2
    }

    override fun part2(input: Input): Number {
        return input.galaxies().expand(ratio = 1_000_000).pairs().sumOf { it.distance() } / 2
    }

}

/*
#.# -> #...#

 */