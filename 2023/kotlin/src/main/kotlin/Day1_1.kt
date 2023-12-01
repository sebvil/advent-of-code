data object Day1_1 : Day<List<Int>>() {

    override val day: Int = 1
    override val part: Int = 1

    override fun parseFile(file: List<String>): List<Int> {
        return file.map { row ->
            val first = row.first { it.isDigit() }
            val last = row.last { it.isDigit() }
            "$first$last".toInt()
        }
    }

    override fun solve(): String {
        return input.sum().toString()
    }
}