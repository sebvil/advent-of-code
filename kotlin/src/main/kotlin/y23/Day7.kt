package y23

import utils.*
import y23.Day7.p2Type

data object Day7 : Day<List<Day7.Input>> {

    enum class Card(val stringRep: String) {
        Two("2"),
        Three("3"),
        Four("4"),
        Five("5"),
        Six("6"),
        Seven("7"),
        Eight("8"),
        Nine("9"),
        T("T"),
        J("J"),
        Q("Q"),
        K("K"),
        A("A");

        fun p2Value(): Int {
            return Card.entries.toMutableList().apply {
                removeAt(J.ordinal)
                add(0, J)
            }.indexOf(this)
        }

        companion object {
            fun fromString(s: String): Card {
                return Card.entries.first { it.stringRep == s }
            }
        }
    }

    data class Input(val hand: List<String>, val bid: Long) : Comparable<Input> {
        override fun compareTo(other: Input): Int {
            val thisHand = hand.map { Card.fromString(it) }
            val otherHand = other.hand.map { Card.fromString(it) }
            return when {
                thisHand.type() < otherHand.type() -> -1
                thisHand.type() > otherHand.type() -> 1
                else -> {
                    thisHand.zip(otherHand).forEach { (thisCard, otherCard) ->
                        if (thisCard < otherCard) {
                            return -1
                        } else if (thisCard > otherCard) {
                            return 1
                        }
                    }
                    0
                }
            }
        }

        fun toInput2(): Input2 = Input2(hand, bid)

    }

    data class Input2(val hand: List<String>, val bid: Long) : Comparable<Input2> {
        override fun compareTo(other: Input2): Int {
            val thisHand = hand.map { Card.fromString(it) }
            val otherHand = other.hand.map { Card.fromString(it) }
            return when {
                thisHand.p2Type() < otherHand.p2Type() -> -1
                thisHand.p2Type() > otherHand.p2Type() -> 1
                else -> {
                    thisHand.zip(otherHand).forEach { (thisCard, otherCard) ->
                        if (thisCard.p2Value() < otherCard.p2Value()) {
                            return -1
                        } else if (thisCard.p2Value() > otherCard.p2Value()) {
                            return 1
                        }
                    }
                    0
                }
            }

        }

    }

    fun List<Card>.type(): Int {
        val counts = buildMap<Card, Int> {
            this@type.forEach {
                put(it, getOrDefault(it, 0) + 1)
            }
        }.values.sorted()
        return when (counts) {
            listOf(5) -> 7
            listOf(1, 4) -> 6
            listOf(2, 3) -> 5
            listOf(1, 1, 3) -> 4
            listOf(1, 2, 2) -> 3
            listOf(1, 1, 1, 2) -> 2
            else -> 1
        }

    }

    fun List<Card>.p2Type(): Int {
        val counts = buildMap<Card, Int> {
            this@p2Type.forEach {
                put(it, getOrDefault(it, 0) + 1)
            }
        }
        val jCount = counts[Card.J] ?: 0
        return when (counts.values.sorted()) {
            listOf(5) -> 7
            listOf(1, 4) -> {
                when (jCount) {
                    1, 4 -> 7
                    else -> 6
                }
            }

            listOf(2, 3) -> {
                when (jCount) {
                    2, 3 -> 7
                    else -> 5
                }
            }

            listOf(1, 1, 3) -> {
                when (jCount) {
                    1, 3 -> 6
                    else -> 4
                }
            }

            listOf(1, 2, 2) -> {
                when (jCount) {
                    1 -> 5
                    2 -> 6
                    else -> 3
                }
            }

            listOf(1, 1, 1, 2) -> {
                when (jCount) {
                    1, 2 -> 4
                    else -> 2
                }
            }

            else -> {
                when (jCount) {
                    1 -> 2
                    else -> 1
                }
            }
        }
    }

    override val day: Int = 7
    override val part1TestInput: String = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 6440L // TODO
    override val part2Expected: Number = 5905L // TODO

    override fun parseFile(file: List<String>): List<Input> {

        return file.map { line ->
            instanceFromRegex(
                text = line,
                pattern = "(.*) (\\d+)"
            ) {
                regex("(.)") forGroup 0
                regex("(.*)") forGroup 1
            }
        }
    }

    override fun part1(input: List<Input>): Number {
        return input.sorted().mapIndexed { idx, it ->
            it.hand.joinToString("").alsoPrint()
            Pair(idx + 1, it.bid)
        }.alsoPrint { it.size.toString() }.sumOf { it.first * it.second }
    }

    override fun part2(input: List<Input>): Number {
        return input.map { it.toInput2() }.sorted().mapIndexed { idx, it ->
            it.hand.alsoPrint(pretty = false) {  h -> "${h.joinToString("")}, ${it.hand.map { c -> Card.fromString(c) }.p2Type()}" }

            Pair(idx + 1, it.bid)
        }.alsoPrint { it.size.toString() }.sumOf { it.first * it.second }
    }

}