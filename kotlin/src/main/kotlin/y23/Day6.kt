package y23

import utils.*

data object Day6 : Day<List<Day6.Input>> {
    data class Input(val times: List<Long>, val distances: List<Long>)

    override val day: Int = 6
    override val part1TestInput: String = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 288L
    override val part2Expected: Number = 71503L

    override fun parseFile(file: List<String>): List<Input> {
        return listOf(instanceFromRegex<Input>(
            text = file.joinToString(),
            pattern = "Time:\\s*(.*),\\s+Distance:\\s*(.*)"
        ) {
            regex("(\\d+)") forGroup 0
            regex("(\\d+)") forGroup 1
        }
        )
    }

    private fun waysToWin(time: Long, record: Long): Long {
        var wins = 0L
        (0..time).forEach { timeHeld ->
            val remainingTime = time - timeHeld
            val distanceTraveled = timeHeld * remainingTime
            if (distanceTraveled > record) {
                wins++
            }
        }
        return wins


    }

    override fun part1(input: List<Input>): Number {
        val actualInput = input.first()
        return actualInput.times.indices.fold(1) { acc: Long, i: Int ->
            acc * waysToWin(actualInput.times[i], actualInput.distances[i])
        }
    }

    override fun part2(input: List<Input>): Number {
        val actualInput = input.first()
        val time = actualInput.times.joinToString("").toLong()
        val distance = actualInput.distances.joinToString("").toLong()

        return waysToWin(time, distance)
    }

}