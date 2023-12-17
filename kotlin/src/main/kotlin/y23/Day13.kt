package y23

import utils.*

data object Day13 : Day<List<Day13.Input>> {


    data class Input(val patterns: List<List<String>>) // TODO()

    override val day: Int = 13
    override val part1TestInput: String = """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.
        
        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 405
    override val part2Expected: Number = 400

    override fun parseFile(file: List<String>): List<Input> {
        val groups = Regex("(.+?)(?>,,|$)").findAll(file.joinToString(",")).map { it.groupValues.last() }.toList()

        return groups.map { group ->
            Input(group.split(",").map {
                instancesFromRegex(
                    clazz = String::class,
                    text = it,
                    pattern = "(.)"
                )
            })
        }
    }

    private fun findSymmetryAxis(grid: Grid<String>): Int? {
        (0..grid.size - 2).forEach outer@ { axis ->
            (0..minOf(axis, grid.size - axis - 2)).forEach {
                val above = grid[axis - it]
                val below = grid[axis + it + 1]
                if (above != below) {
                    return@outer
                }
            }
            return  axis + 1
        }
        return null
    }

    private fun findSymmetryAxisP2(grid: Grid<String>): Int? {
        (0..grid.size - 2).forEach outer@ { axis ->
            var invalidCount = 0
            var invalidAbove: List<String> = listOf()
            var invalidBelow: List<String> = listOf()
            (0..minOf(axis, grid.size - axis - 2)).forEach {
                val above = grid[axis - it]
                val below = grid[axis + it + 1]
                if (above != below) {
                    invalidCount++
                    if (invalidCount > 1) {
                        return@outer
                    }
                    invalidAbove = above
                    invalidBelow = below
                }
            }
            if (invalidCount == 1) {
                val invalidCells = invalidAbove.zip(invalidBelow).filter { (f, s) ->
                    f != s
                }.size
                if (invalidCells == 1)  {
                    return axis + 1
                }
            }
        }
        return null
    }


    override fun part1(input: List<Input>): Number {
        return input.sumOf {
            findSymmetryAxis(it.patterns)?.times(100) ?: findSymmetryAxis(it.patterns.transpose())!!
        }
    }

    override fun part2(input: List<Input>): Number {
        return input.sumOf {
            findSymmetryAxisP2(it.patterns)?.times(100) ?: findSymmetryAxisP2(it.patterns.transpose())!!
        }
    }

}