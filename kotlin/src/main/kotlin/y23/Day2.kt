package y23

import utils.*

data object Day2 : Day<List<Day2.Input>> {
    data class Cube(val count: Int, val color: Color)
    enum class Color {
        Red,
        Green,
        Blue
    }

    data class CubeSet(val cubes: List<Cube>) {
        fun isInvalid(): Boolean {
            return cubes.map {
                when (it.color) {
                    Color.Red -> it.count > 12
                    Color.Green -> it.count > 13
                    Color.Blue -> it.count > 14
                }
            }.any { it }
        }
    }

    data class Input(val id: Int, val cubeSets: List<CubeSet>) {
        fun isInvalid(): Boolean {
            return cubeSets.map { it.isInvalid() }.any { it }
        }
    }


    override val day: Int = 2
    override val part1TestInput: String = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()
    override val part2TestInput: String = part1TestInput
    override val part1Expected: Number = 8
    override val part2Expected: Number = 2286

    override fun parseFile(file: List<String>): List<Input> {
        return file.map { line ->
            instanceFromRegex(
                text = line,
                pattern = "Game ([0-9]*): (.*)"
            ) {
                regex(pattern = "(.*?;|.+)") {
                    regex(pattern = "([0-9]*) (red|green|blue)") forGroup 0
                } forGroup 1
            }

        }
    }

    override fun part1(input: List<Input>): Number {
        return input.filter { game ->
            !game.isInvalid()
        }.sumOf { it.id }
    }

    override fun part2(input: List<Input>): Number {
        return input.sumOf { game ->
            game.cubeSets.maxOf { cubeSet ->
                cubeSet.cubes.find { it.color == Color.Red }?.count ?: 0
            } * game.cubeSets.maxOf { cubeSet ->
                cubeSet.cubes.find { it.color == Color.Blue }?.count ?: 0
            } * game.cubeSets.maxOf { it.cubes.find { cubeSet -> cubeSet.color == Color.Green }?.count ?: 0 }
        }
    }
}