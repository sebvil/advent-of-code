import utils.*

data object Day2 : Day<List<Day2.Game>>() {
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

    data class Game(val id: Int, val cubeSets: List<CubeSet>) {
        fun isInvalid(): Boolean {
            return cubeSets.map { it.isInvalid() }.any { it }
        }
    }


    override val day: Int = 2
    override fun parseFile(file: List<String>): List<Game> {
        return file.map { line ->
            instanceFromRegex<Game>(
                text = line,
                regex = Regex(pattern = "Game ([0-9]*): (.*)")
            ) {
                regex(regex = Regex(pattern = "(.*?;|.+)")) {
                    regex(Regex(pattern = "([0-9]*) (red|green|blue)")) forGroup 0
                } forGroup 1
            }

        }
    }

    override fun part1(): Number {
        return input.filter { game ->
            !game.isInvalid()
        }.sumOf { it.id }
    }

    override fun part2(): Number {
        return input.sumOf { game ->
            game.cubeSets.maxOf { cubeSet ->
                cubeSet.cubes.find { it.color == Color.Red }?.count ?: 0
            } * game.cubeSets.maxOf { cubeSet ->
                cubeSet.cubes.find { it.color == Color.Blue }?.count ?: 0
            } * game.cubeSets.maxOf { it.cubes.find { cubeSet -> cubeSet.color == Color.Green }?.count ?: 0 }
        }
    }
}