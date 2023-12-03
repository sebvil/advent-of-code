package utils

fun String.toNum(lowerCaseOffset: Int, upperCaseOffset: Int): Int {
    val char = this.first().code
    val lowerA = 'a'.code
    val upperA = 'A'.code
    val lowerZ = 'z'.code
    return if (char in lowerA..lowerZ) {
        char - lowerA + lowerCaseOffset
    } else {
        char - upperA + upperCaseOffset
    }
}

fun Char.toNum(lowerCaseOffset: Int, upperCaseOffset: Int): Int {
    val char = this.code
    val lowerA = 'a'.code
    val upperA = 'A'.code
    val lowerZ = 'z'.code
    return if (char in lowerA..lowerZ) {
        char - lowerA + lowerCaseOffset
    } else {
        char - upperA + upperCaseOffset
    }
}