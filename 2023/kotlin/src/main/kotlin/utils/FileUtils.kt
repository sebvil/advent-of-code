package utils

import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(day: Int): List<String> {
    return Path("src/main/kotlin/inputs/day$day.txt").readLines()
}
