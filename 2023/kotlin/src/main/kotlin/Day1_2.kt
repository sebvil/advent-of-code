data object Day1_2 : Day<List<Int>>() {

    override val day: Int = 1
    override val part: Int = 2

    override fun parseFile(file: List<String>): List<Int> {
        return file.map { row ->
            val firstFound = digits.filter { row.indexOf(it) != -1 }.minByOrNull { row.indexOf(it) }!!.toDigit()
            val lastFound = digits.filter { row.indexOf(it) != -1 }.maxByOrNull { row.lastIndexOf(it) }!!.toDigit()
            "$firstFound$lastFound".toInt()
        }
    }

    private val digits = (1..9).map { "$it" } + listOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    )

    private fun String.toDigit(): Int {
        return (digits.indexOf(this) % 9) + 1
    }


    override fun solve(): Int {
        return input.sum()
    }
}