package y23

import BaseDay

sealed interface Day<Input> : BaseDay<Input> {
    override val year: Int
        get() = 23
}