package y23

import utils.*

data object Day10 : Day<List<Day10.PreProcessed>> {

    enum class Direction {
        N, W, S, E;

        companion object {
            fun fromCells(from: Cell, to: Cell): Direction {
                return when {
                    from.column == to.column && from.row < to.row -> S
                    from.column == to.column && from.row > to.row -> N
                    from.row == to.row && from.column < to.column -> E
                    from.row == to.row && from.column > to.column -> W
                    else -> error("Invalid cells")
                }
            }
        }
    }

    enum class Pipe(val cell: String) {
        Vertical("|"),
        Horizontal("-"),
        NE("L"),
        NW("J"),
        SW("7"),
        SE("F"),
        Ground("."),
        Animal("S");

        fun isValidConnection(direction: Direction, other: Pipe): Boolean {
            return when (direction) {
                Direction.N -> when (this) {
                    Vertical, NE, NW, Animal -> other in listOf(Vertical, SW, SE)
                    else -> false
                }

                Direction.W -> when (this) {
                    Horizontal, SW, NW, Animal -> other in listOf(Horizontal, NE, SE)
                    else -> false
                }

                Direction.S -> when (this) {
                    Vertical, SW, SE, Animal -> other in listOf(Vertical, NE, NW)
                    else -> false
                }

                Direction.E -> when (this) {
                    Horizontal, SE, NE, Animal -> other in listOf(Horizontal, NW, SW)
                    else -> false
                }
            }
        }

        fun isVertical(): Boolean {
            return when (this) {
                Horizontal, Ground -> false
                else -> true
            }
        }

        fun isMatch(other: Pipe?): Boolean {
            return when (this) {
                SW -> other == NE
                NW -> other == SE
                else -> false
            }
        }

        companion object {
            fun fromCell(cell: String): Pipe {
                return Pipe.entries.find { it.cell == cell }!!
            }
        }
    }

    data class Row(val row: List<Pipe>) // TODO()
    data class PreProcessed(val row: List<String>) // TODO()

    override val day: Int = 10
    override val part1TestInput: String = """
        7-F7-
        .FJ|7
        SJLL7
        |F--J
        LJ.LJ
    """.trimIndent()
    override val part2TestInput: String = """
        .F----7F7F7F7F-7....
        .|F--7||||||||FJ....
        .||.FJ||||||||L7....
        FJL7L7LJLJ||LJ.L-7..
        L--J.L7...LJS7F-7L7.
        ....F-J..F7FJ|L7L7L7
        ....L7.F7||L7|.L7L7|
        .....|FJLJ|FJ|F7|.LJ
        ....FJL-7.||.||||...
        ....L---J.LJ.LJLJ...
    """.trimIndent()
    override val part1Expected: Number = 8 // TODO
    override val part2Expected: Number = 8 // TODO

    override fun parseFile(file: List<String>): List<PreProcessed> {

        return file.map { line ->
            instanceFromRegex(
                text = line,
                pattern = "(.*)"
            ) {
                regex("(.)") forGroup 0
            }
        }
    }

    override fun part1(input: List<PreProcessed>): Number {
        val processed = input.map { it.row.map { cell -> Pipe.fromCell(cell) } }
        return getPipesCells(processed).size / 2
    }

    override fun part2(input: List<PreProcessed>): Number {
        val processed = input.map { it.row.map { cell -> Pipe.fromCell(cell) } }
        val pipes = getPipesCells(processed)
        val countsMap = List(processed.size) {
            List(processed[0].size) { 0 }.toMutableList()
        }.toMutableList()
        var insideCount = 0
        var lastVertical: Pipe? = null
        processed.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { col, pipe ->
                val addedVerticalCount = if (Cell(rowIndex, col) in pipes && pipe.isVertical()) {
                    if (pipe.isMatch(lastVertical)) {
                        0
                    } else {
                        1
                    }.also {
                        lastVertical = pipe
                    }
                } else {
                    0
                }
                if (col == 0) {
                    countsMap[rowIndex][col] = addedVerticalCount
                } else {
                    val prev = countsMap[rowIndex][col - 1]
                    if (Cell(rowIndex, col) !in pipes) {
                        insideCount += prev % 2
                    }
                    countsMap[rowIndex][col] = prev + addedVerticalCount
                }
            }
        }
        return insideCount
    }


    private fun getPipesCells(grid: Grid<Pipe>): Set<Cell> {
        val startRow = grid.indexOfFirst { Pipe.Animal in it }
        val startColumn = grid[startRow].indexOf(Pipe.Animal)
        var next: Pipe?
        var currentCell = Cell(startRow, startColumn)
        val visited = mutableSetOf(currentCell)
        while (true) {
            next = null
            for (cell in getAdjacentCells(grid, currentCell, includeDiagonals = false)) {
                if (cell !in visited) {
                    val direction = Direction.fromCells(from = currentCell, to = cell)
                    val pipe = grid[cell.row][cell.column]
                    if (grid[currentCell.row][currentCell.column].isValidConnection(direction, pipe)) {
                        next = pipe
                        currentCell = cell
                        visited.add(cell)
                        break
                    }
                }
            }
            if (next == null) {
                break
            }
        }
        return visited
    }

}