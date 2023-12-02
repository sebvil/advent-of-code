//data object Day2_2 : Day<List<Game>>() {
//
//    override val day: Int = 2
//    override val part: Int = 2
//
//    override fun parseFile(file: List<String>): List<Game> {
//        return file.mapIndexed { idx, line ->
//            Game(
//                cubeSets = line.substringAfter(":").split(";").map { game ->
//                    CubeSet(game.split(",").map { cube ->
//                        cube.trim().split(" ").let {
//                            Cube(Color.valueOf(it[1]), it[0].toInt())
//                        }
//                    })
//                }, id = idx + 1
//            )
//
//        }
//    }
//
//    override fun solve(): Any {
//        return input.sumOf { game ->
//            game.cubeSets.maxOf { cubeSet ->
//                cubeSet.cubes.find { it.color == Color.red }?.count ?: 0
//            } * game.cubeSets.maxOf { cubeSet ->
//                cubeSet.cubes.find { it.color == Color.blue }?.count ?: 0
//            } * game.cubeSets.maxOf { it.cubes.find { cubeSet -> cubeSet.color == Color.green }?.count ?: 0 }
//        }
//    }
//}