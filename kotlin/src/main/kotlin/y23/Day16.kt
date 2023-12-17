package y23

import utils.*

data object Day16 : Day<Grid<Day16.CellType>> {

    sealed class CellType(override val stringValue: String) : ParsableEnum<CellType> {
        data object SwToNeMirror : CellType("/")
        data object NwToSeMirror : CellType("\\")
        data object HorizontalSplitter : CellType("-")
        data object VerticalSplitter : CellType("|")
        data object Space : CellType(".")

        companion object {
            fun entries() = listOf(SwToNeMirror, NwToSeMirror, HorizontalSplitter, VerticalSplitter, Space)
        }
    }

    enum class Direction { N, W, S, E }

    data class Input(val row: List<CellType>)

    private fun List<Input>.toGrid(): Grid<CellType> {
        return map { it.row }
    }

    private fun Cell.move(direction: Direction): Cell {
        return when (direction) {
            Direction.N -> copy(row = row - 1)
            Direction.W -> copy(column = column - 1)
            Direction.S -> copy(row = row + 1)
            Direction.E -> copy(column = column + 1)
        }
    }

    override val day: Int = 16
    override val part1TestInput: String = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 46
    override val part2Expected: Number = 51

    override fun parseFile(file: List<String>): Grid<CellType> {

        return file.map { line ->
            instanceFromRegex<Input>(
                text = line,
                pattern = "(.*)"
            ) {
                regex("([.|\\-/\\\\])") forGroup 0
            }
        }.toGrid()
    }

    override fun part1(input: Grid<CellType>): Number {
        val energizedCells: MutableSet<Cell> = mutableSetOf()
        val visited: MutableSet<Pair<Cell, Direction>> = mutableSetOf()
        fun visitCell(cell: Cell, direction: Direction) {
            if (cell to direction in visited) {
                return
            }
            visited.add(cell to direction)
            if (cell in input) {
                energizedCells.add(cell)
                when (input[cell]) {
                    CellType.Space -> {
                        visitCell(cell.move(direction), direction)
                    }

                    CellType.HorizontalSplitter -> {
                        if (direction == Direction.W || direction == Direction.E) {
                            visitCell(cell.move(direction), direction)
                        } else {
                            visitCell(cell.move(Direction.W), direction = Direction.W)
                            visitCell(cell.move(Direction.E), direction = Direction.E)
                        }
                    }

                    CellType.VerticalSplitter -> {
                        if (direction == Direction.N || direction == Direction.S) {
                            visitCell(cell.move(direction), direction)
                        } else {
                            visitCell(cell.move(Direction.N), direction = Direction.N)
                            visitCell(cell.move(Direction.S), direction = Direction.S)
                        }
                    }

                    CellType.NwToSeMirror -> {
                        val newDirection = when (direction) {
                            Direction.N -> Direction.W
                            Direction.W -> Direction.N
                            Direction.S -> Direction.E
                            Direction.E -> Direction.S
                        }
                        visitCell(cell.move(newDirection), newDirection)
                    }

                    CellType.SwToNeMirror -> {
                        val newDirection = when (direction) {
                            Direction.N -> Direction.E
                            Direction.W -> Direction.S
                            Direction.S -> Direction.W
                            Direction.E -> Direction.N
                        }
                        visitCell(cell.move(newDirection), newDirection)
                    }

                }
            }
        }
        visitCell(cell = Cell(row = 0, column = 0), direction = Direction.E)

        return energizedCells.size
    }

    override fun part2(input: Grid<CellType>): Number {
        val energizedCounts: MutableMap<Pair<Cell, Direction>, Int> = mutableMapOf()
        val visited: MutableSet<Pair<Cell, Direction>> = mutableSetOf()
        fun visitCell(cell: Cell, direction: Direction, energized: MutableSet<Cell>): Int {
            if (cell !in input) return 0
            if (cell to direction in visited) {
                return 0
            }
            visited.add(cell to direction)
            energizedCounts[cell to direction]?.let { return it }
            energized.add(cell)
            return when (input[cell]) {
                CellType.Space -> {
                    visitCell(cell = cell.move(direction), direction = direction, energized)
                }

                CellType.HorizontalSplitter -> {
                    if (direction == Direction.W || direction == Direction.E) {
                        visitCell(cell = cell.move(direction), direction = direction, energized)
                    } else {
                        visitCell(cell = cell.move(Direction.W), direction = Direction.W, energized) + visitCell(
                            cell = cell.move(Direction.E),
                            direction = Direction.E,
                            energized
                        )
                    }
                }

                CellType.VerticalSplitter -> {
                    if (direction == Direction.N || direction == Direction.S) {
                        visitCell(cell.move(direction), direction, energized)
                    } else {
                        visitCell(cell = cell.move(Direction.N), direction = Direction.N, energized)
                        visitCell(
                            cell = cell.move(
                                Direction.S
                            ),
                            direction = Direction.S,
                            energized
                        )
                    }
                }

                CellType.NwToSeMirror -> {
                    val newDirection = when (direction) {
                        Direction.N -> Direction.W
                        Direction.W -> Direction.N
                        Direction.S -> Direction.E
                        Direction.E -> Direction.S
                    }
                    visitCell(cell = cell.move(newDirection), direction = newDirection, energized)
                }

                CellType.SwToNeMirror -> {
                    // "/"
                    val newDirection = when (direction) {
                        Direction.N -> Direction.E
                        Direction.W -> Direction.S
                        Direction.S -> Direction.W
                        Direction.E -> Direction.N
                    }
                    visitCell(cell = cell.move(newDirection), direction = newDirection, energized)
                }
            }.let {
                val res = energized.size
                energizedCounts[cell to direction] = res
                res
            }
        }

        val indices =
            input.indices.flatMap {
                listOf(
                    Cell(row = it, column = 0) to Direction.E,
                    Cell(row = it, column = input[0].lastIndex) to Direction.W
                )
            } + input[0].indices.flatMap {
                listOf(
                    Cell(row = 0, column = it) to Direction.S,
                    Cell(row = input.lastIndex, column = it) to Direction.N
                )
            }
        return indices.maxOf {
            visitCell(it.first, it.second, mutableSetOf()).also {
                visited.clear()
                energizedCounts.clear()
            }

        }
    }
}