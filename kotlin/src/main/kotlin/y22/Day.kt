package y22

import BaseDay

sealed interface Day<Input>  : BaseDay<Input> {
    override val year: Int
        get() = 22
}