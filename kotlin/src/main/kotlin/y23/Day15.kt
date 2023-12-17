package y23

import utils.*

data object Day15 : Day<Day15.Input> {
    data class Input(val initSequence: List<String>)

    sealed interface SequenceStep {
        val label: String

        data class Add(override val label: String, val focalLength: Int): SequenceStep
        data class Remove(override val label: String): SequenceStep

        companion object {
            fun fromString(string: String): SequenceStep {
                return when {
                    '=' in string -> instanceFromRegex<Add>(string, "(.+)=(\\d+)") {
                        regex("(.+)") forGroup 0
                        regex("(\\d+)") forGroup 1
                    }
                    else ->  instanceFromRegex<Remove>(string, "(.+)-") {
                        regex("(.+)") forGroup 0
                    }
                }
            }

        }

    }

    override val day: Int = 15
    override val part1TestInput: String = """
        rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 1320L
    override val part2Expected: Number = 145L

    override fun parseFile(file: List<String>): Input {

        return instanceFromRegex(
            text = file.first(),
            pattern = "(.*)"
        ) {
            regex("(.+?)(?>,|$)") forGroup 0
        }
    }


    private fun hash(currentValue: Long = 0L, sequence: String): Long {
        if (sequence.isEmpty()) return currentValue
        var res = currentValue
        val chr = sequence.first()
        res += chr.code
        res *= 17
        res %= 256
        return hash(res, sequence.substring(1))
    }

    override fun part1(input: Input): Number {
        return input.initSequence.sumOf { hash(0, it) }
    }

    override fun part2(input: Input): Number {
        val initSequence = input.initSequence.map { SequenceStep.fromString(it) }
        val boxes: MutableMap<Long, MutableList<SequenceStep.Add>> = mutableMapOf()
        initSequence.forEach { step ->
            val hash = hash(0, step.label)
            val box = boxes.getOrPut(hash) { mutableListOf() }
            val index = box.indexOfFirst { it.label ==  step.label}
            when (step) {
                is SequenceStep.Add -> {
                    when (index) {
                        -1 -> boxes[hash]!! += step
                        else -> boxes[hash]!![index] = step
                    }
                }
                is SequenceStep.Remove -> {
                    when (index) {
                        -1 -> Unit
                        else ->  boxes[hash]!!.removeAt(index)
                    }

                }
            }
        }
        return boxes.map { (boxIndex, box) ->
            box.mapIndexed { index, lens ->
                (boxIndex + 1) * (index + 1) * lens.focalLength
            }.sumOf { it }
        }.sumOf { it }
    }

}