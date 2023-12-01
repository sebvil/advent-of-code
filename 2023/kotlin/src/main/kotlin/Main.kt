import utils.println

fun main() {
    val solution = Day::class.sealedSubclasses
        .mapNotNull { it.objectInstance }
        .maxByOrNull { it.day * 2 + it.part }!!
        .solve()
    solution.println()
}