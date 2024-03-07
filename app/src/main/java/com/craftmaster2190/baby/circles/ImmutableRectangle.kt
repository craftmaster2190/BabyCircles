package com.craftmaster2190.baby.circles

import kotlin.math.max
import kotlin.math.min

data class ImmutableRectangle(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    companion object {
        @JvmStatic
        fun from(x1: Float, y1: Float, x2: Float, y2: Float): ImmutableRectangle {
            return ImmutableRectangle(min(x1, x2), min(y1, y2), max(x1, x2), max(y1, y2))
        }
    }
}
