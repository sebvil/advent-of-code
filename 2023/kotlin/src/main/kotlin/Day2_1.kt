import utils.println
import kotlin.math.ln

data class Cube(val color: Color, val count: Int)
enum class Color {
    red,
    green,
    blue
}

data class CubeSet(val cubes: List<Cube>) {

    init {
        check(cubes.size <= 3)
    }

    fun isInvalid(): Boolean {
        return cubes.map {
            when (it.color) {
                Color.red -> it.count > 12
                Color.green -> it.count > 13
                Color.blue -> it.count > 14
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
        return file.mapIndexed { idx, line ->
            Game(
                cubeSets = line.substringAfter(":").split(";").map { game ->
                    CubeSet(game.split(",").map { cube ->
                        cube.trim().split(" ").let {
                            Cube(Color.valueOf(it[1]), it[0].toInt())
                        }
                    })
                }, id = idx + 1
            )

        }
    }

    override fun solve(): Any {
        return input.filter { game ->
            !game.isInvalid()
        }.sumOf { it.id }
    }
}