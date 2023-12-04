package y22

import utils.*

data object Day2 : Day<List<Day2.Input>> {

    enum class Shape(val score: Int) {
        Rock(1) {
            override val loser: Shape
                get() = Paper
            override val winner: Shape
                get() = Scissors
        },
        Paper(2) {
            override val loser: Shape
                get() = Scissors
            override val winner: Shape
                get() = Rock
        },
        Scissors(3) {
            override val loser: Shape
                get() = Rock
            override val winner: Shape
                get() = Paper
        },;


        abstract val winner: Shape
        abstract val loser: Shape
       fun versus(other: Shape): Int {
           return when (this) {
               Rock -> when (other) {
                   Rock -> 3
                   Paper -> 0
                   Scissors -> 6
               }
               Paper -> when (other) {
                   Rock -> 6
                   Paper -> 3
                   Scissors -> 0
               }
               Scissors -> when (other) {
                   Rock -> 0
                   Paper -> 6
                   Scissors -> 3
               }
           }
       }
    }
    enum class Opponent(val shape: Shape) {
        A(Shape.Rock), B(Shape.Paper), C(Shape.Scissors);
    }
    enum class Player {
        X, Y, Z
    }

    data class Game(val first: Opponent, val second: Player) : RegexParsable
    data class Input(val game: Game)
    override val day: Int = 2
    override val part1TestInput: String = """
        A Y
        B X
        C Z
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 15
    override val part2Expected: Number = 12

    override fun parseFile(file: List<String>): List<Input> {
        return file.map { line ->
            instanceFromRegex(
                text = line,
                pattern = "(.+)"
            ) {
                regex(pattern = "([A-C]) ([X-Z])") forGroup 0
            }
        }
    }

    override fun part1(input: List<Input>): Number {
        return input.map {
            val game = it.game
            when (game.second) {
                Player.X -> Pair(game.first.shape, Shape.Rock)
                Player.Y -> Pair(game.first.shape, Shape.Paper)
                Player.Z -> Pair(game.first.shape, Shape.Scissors)
            }
        }.fold(0) { acc, game ->
            acc + game.second.score + game.second.versus(game.first)
        }
    }

    override fun part2(input: List<Input>): Number {
        return input.map {
            val shape = it.game.first.shape
            when (it.game.second) {
                Player.X -> Pair(shape, shape.winner)
                Player.Y -> Pair(shape, shape)
                Player.Z -> Pair(shape, shape.loser)
            }
        }.fold(0) { acc, game ->
            acc + game.second.score + game.second.versus(game.first)
        }    }

}