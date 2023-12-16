package utils

typealias Grid<T> = List<List<T>>

data class Cell(val row: Int, val column: Int)

fun getAdjacentCells(grid: Grid<*>, row: Int, column: Int, includeDiagonals: Boolean = true): List<Cell> {
    return ((row - 1)..(row + 1)).filter { it in grid.indices }.flatMap { r ->
        ((column - 1)..(column + 1)).filter { it in grid[0].indices }.mapNotNull { c ->
            when {
                (r != row || c != column) && includeDiagonals -> Cell(r, c)
                (r == row || c == column) && !(r == row && c == column) -> Cell(r, c)
                else -> null
            }
        }
    }
}

fun getAdjacentCells(grid: Grid<*>, cell: Cell, includeDiagonals: Boolean = true): List<Cell> {
    return getAdjacentCells(grid, cell.row, cell.column, includeDiagonals)
}