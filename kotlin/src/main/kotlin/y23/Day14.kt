package y23

import utils.*

data object Day14 : Day<Grid<Day14.CellType>> {

    sealed class CellType(override val stringValue: String) : ParsableEnum<CellType> {

        data object Space : CellType(".")
        data object SquareRock : CellType("#")
        data object CircleRock : CellType("O")

        companion object {
            fun entries(): List<CellType> = listOf(Space, SquareRock, CircleRock)
        }
    }

    data class Input(val row: List<CellType>)

    override val day: Int = 14
    override val part1TestInput: String = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 136
    override val part2Expected: Number = 64 // TODO

    override fun parseFile(file: List<String>): Grid<CellType> {

        return file.map { line ->
            instanceFromRegex<Input>(
                text = line,
                pattern = "(.*)"
            ) {
                regex("(.)") forGroup 0
            }.row
        }
    }

    override fun part1(input: Grid<CellType>): Number {
        return input.tiltNorth().mapIndexed { index, cellTypes ->
            cellTypes to (input.size - index)
        }.sumOf { (cells, load) ->
            cells.count { it is CellType.CircleRock } * load
        }
    }

    private fun Grid<CellType>.tiltNorth(): Grid<CellType> {
        val res: MutableList<MutableList<CellType>> =
            map { it.map { CellType.Space }.toMutableList<CellType>() }.toMutableList()
        forEachIndexed { rowIndex, row ->
            row.forEachIndexed { column, cell ->
                when (cell) {
                    CellType.CircleRock -> {
                        var r = rowIndex - 1
                        while (r > -1) {
                            val candidate = res[r][column]
                            if (candidate !is CellType.Space) {
                                break
                            }
                            r--
                        }
                        res[r + 1][column] = CellType.CircleRock

                    }

                    CellType.Space -> Unit
                    CellType.SquareRock -> res[rowIndex][column] = CellType.SquareRock
                }
            }
        }
        return res
    }

    override fun part2(input: Grid<CellType>): Number {
        var grid = input
        val seenConfigs: MutableMap<Grid<CellType>, Int> = mutableMapOf()
        val seenConfigsReverseMap: MutableMap<Int, Grid<CellType>> = mutableMapOf()

        val totalIterations = 1000000000
        var loopOffset = 0
        for (i in 0..<totalIterations) {
            if (grid in seenConfigs) {
                loopOffset = seenConfigs[grid]!!
                break
            }
            seenConfigs[grid] = i
            seenConfigsReverseMap[i] = grid
            grid = grid
                .tiltNorth().rotateRight()
                .tiltNorth().rotateRight()
                .tiltNorth().rotateRight()
                .tiltNorth().rotateRight()
            grid.forEach {
                it.alsoPrint { a -> a.joinToString("") { b -> b.stringValue } }
            }
            kotlin.io.println()
        }
        val loops = seenConfigs.size - loopOffset

        val finalGrid = seenConfigsReverseMap[((totalIterations - loopOffset) % loops) + loopOffset]!!


        return finalGrid.mapIndexed { index, cellTypes ->
            cellTypes to (input.size - index)
        }.sumOf { (cells, load) ->
            cells.count { it is CellType.CircleRock } * load
        }
    }

}
