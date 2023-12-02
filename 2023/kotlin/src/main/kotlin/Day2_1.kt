import utils.*

data class Cube(val count: Int, val color: Color)
enum class Color {
    Red,
    Green,
    Blue
}

data class CubeSet(val cubes: List<Cube>) {

    init {
        check(cubes.size <= 3)
    }

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

data object Day2_1 : Day<List<Game>>() {

    override val day: Int = 2
    override val part: Int = 1
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

    override fun solve(): Any {
        return input.filter { game ->
            !game.isInvalid()
        }.sumOf { it.id }
    }
}