package utils

fun lcm(numbers: MutableList<Int>): Long {
    var res: Long = 1
    var divisor = 2
    while (true) {
        var counter = 0
        var divisible = false
        for (i in numbers.indices) {

            // lcm_of_array_elements (n1, n2, ... 0) = 0.
            // For negative number we convert into
            // positive and calculate lcm_of_array_elements.
            if (numbers[i] == 0) {
                return 0
            } else if (numbers[i] < 0) {
                numbers[i] = numbers[i] * -1
            }
            if (numbers[i] == 1) {
                counter++
            }

            // Divide element_array by divisor if complete
            // division i.e. without remainder then replace
            // number with quotient; used for find next factor
            if (numbers[i] % divisor == 0) {
                divisible = true
                numbers[i] = numbers[i] / divisor
            }
        }

        // If divisor able to completely divide any number
        // from array multiply with lcm_of_array_elements
        // and store into lcm_of_array_elements and continue
        // to same divisor for next factor finding.
        // else increment divisor
        if (divisible) {
            res *= divisor
        } else {
            divisor++
        }

        // Check if all element_array is 1 indicate
        // we found all factors and terminate while loop.
        if (counter == numbers.size) {
            return res
        }
    }
}