package utils

import kotlin.io.path.Path
import kotlin.io.path.readLines

/*
* Reads lines from the given input txt file.
*/
fun readInput(day: Int) = Path("/home/sebvil/dev/src/advent-of-code/2023/kotlin/src/main/kotlin/inputs/day$day.txt").readLines()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun Any?.alsoPrint() = also { it.println() }