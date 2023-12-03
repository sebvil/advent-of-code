package utils

import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(year: Int, day: Int): List<String> {
    return Path("src/main/kotlin/y$year/inputs/day$day.txt").readLines()
}
