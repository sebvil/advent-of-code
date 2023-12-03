package utils

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class CollectionUtilsKtTest {

    @Test
    fun getAdjacentCells() {
        val grid = List(10) { a -> List(10) { b -> a * 10 + b } }

        // Include diagonals
        // First row
        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 0, column = 0)
        ).containsExactly(
            Cell(row = 0, column = 1),
            Cell(row = 1, column = 0),
            Cell(row = 1, column = 1)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 0, column = 1)
        ).containsExactly(
            Cell(row = 0, column = 0),
            Cell(row = 0, column = 2),
            Cell(row = 1, column = 0),
            Cell(row = 1, column = 1),
            Cell(row = 1, column = 2)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 0, column = 9)
        ).containsExactly(
            Cell(row = 0, column = 8),
            Cell(row = 1, column = 8),
            Cell(row = 1, column = 9)
        )


        // Second row
        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 1, column = 0)
        ).containsExactly(
            Cell(row = 0, column = 0),
            Cell(row = 0, column = 1),
            Cell(row = 1, column = 1),
            Cell(row = 2, column = 0),
            Cell(row = 2, column = 1)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 1, column = 1)
        ).containsExactly(
            Cell(row = 0, column = 0),
            Cell(row = 0, column = 1),
            Cell(row = 0, column = 2),
            Cell(row = 1, column = 0),
            Cell(row = 1, column = 2),
            Cell(row = 2, column = 0),
            Cell(row = 2, column = 1),
            Cell(row = 2, column = 2)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 1, column = 9)
        ).containsExactly(
            Cell(row = 0, column = 8),
            Cell(row = 0, column = 9),
            Cell(row = 1, column = 8),
            Cell(row = 2, column = 8),
            Cell(row = 2, column = 9)
        )

        // Last row
        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 9, column = 0)
        ).containsExactly(
            Cell(row = 8, column = 0),
            Cell(row = 8, column = 1),
            Cell(row = 9, column = 1)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 9, column = 1)
        ).containsExactly(
            Cell(row = 8, column = 0),
            Cell(row = 8, column = 1),
            Cell(row = 8, column = 2),
            Cell(row = 9, column = 0),
            Cell(row = 9, column = 2)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 9, column = 9)
        ).containsExactly(
            Cell(row = 8, column = 8),
            Cell(row = 8, column = 9),
            Cell(row = 9, column = 8)
        )

        // Do not include diagonals
        // First row
        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 0, column = 0, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 0, column = 1),
            Cell(row = 1, column = 0),
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 0, column = 1, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 0, column = 0),
            Cell(row = 0, column = 2),
            Cell(row = 1, column = 1),
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 0, column = 9, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 0, column = 8),
            Cell(row = 1, column = 9)
        )


        // Second row
        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 1, column = 0, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 0, column = 0),
            Cell(row = 1, column = 1),
            Cell(row = 2, column = 0),
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 1, column = 1, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 0, column = 1),
            Cell(row = 1, column = 0),
            Cell(row = 1, column = 2),
            Cell(row = 2, column = 1),
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 1, column = 9, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 0, column = 9),
            Cell(row = 1, column = 8),
            Cell(row = 2, column = 9)
        )

        // Last row
        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 9, column = 0, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 8, column = 0),
            Cell(row = 9, column = 1)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 9, column = 1, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 8, column = 1),
            Cell(row = 9, column = 0),
            Cell(row = 9, column = 2)
        )

        Truth.assertThat(
            getAdjacentCells(grid = grid, row = 9, column = 9, includeDiagonals = false)
        ).containsExactly(
            Cell(row = 8, column = 9),
            Cell(row = 9, column = 8)
        )

    }

}