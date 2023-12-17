package y23

import org.intellij.lang.annotations.Language
import utils.*

data object Day12 : Day<List<Day12.Input>> {
    data class Input(val record: String, val brokenPipes: List<Int>)

    override val day: Int = 12
    override val part1TestInput: String = """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 21L
    override val part2Expected: Number = 525152L

    override fun parseFile(file: List<String>): List<Input> {

        return file.map { line ->
            instanceFromRegex(
                text = line,
                pattern = "(.*) (.*)"
            ) {
                regex("(.+)") forGroup 0
                regex("(\\d+)") forGroup 1
            }
        }
    }

    override fun part1(input: List<Input>): Number {
        return input.sumOf { arrangements(it) }
    }

    private val cache: MutableMap<Input, Long> = mutableMapOf()

    private fun arrangements(record: Input): Long {
        cache[record]?.let { return it }
        if (record.brokenPipes.isEmpty()) {
            return if (Regex("${OPERATIONAL_MATCHER}*").matches(record.record)) {
                1
            } else {
                0
            }
        }
        var count = 0L
        (0..(record.record.length - record.brokenPipes.sumOf { it } - record.brokenPipes.size + 1)).forEach {
            val match =
                Regex("^$OPERATIONAL_MATCHER{$it}$DAMAGED_MATCHER{${record.brokenPipes.first()}}(?>[.?]|\$)").find(
                    record.record
                )
                    ?: return@forEach

            count += arrangements(
                record.copy(
                    record = record.record.substring(startIndex = match.range.last + 1),
                    brokenPipes = record.brokenPipes.drop(1)
                )
            )
        }
        cache[record] = count
        return count
    }

    override fun part2(input: List<Input>): Number {
        val unfolded = input.map {
            arrangements(
                it.copy(
                    record = List(5) { _ -> it.record }.joinToString("?"),
                    brokenPipes = List(5) { _ -> it.brokenPipes }.flatten()
                )
            )
        }




        return unfolded.sumOf { it }
    }


    @Language("RegExp")
    private const val OPERATIONAL_MATCHER = "[.?]"

    @Language("RegExp")
    private const val DAMAGED_MATCHER = "[#?]"
}
