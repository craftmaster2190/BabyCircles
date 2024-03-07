package com.craftmaster2190.baby.circles

import org.hamcrest.BaseMatcher
import org.hamcrest.Description

class IsCloseTo(val expectedValue: Float, val withinDistance: Float = 0f) :
    BaseMatcher<Float>() {
    override fun describeTo(description: Description?) {
        description?.appendValue(expectedValue)?.appendText("+/-")?.appendValue(withinDistance)
    }

    override fun matches(item: Any?): Boolean {
        if (item != null && item is Float) {
            if (item == expectedValue || (item >= (expectedValue - withinDistance) && item <= (expectedValue + withinDistance))) {
                return true;
            }
        }
        return false;
    }

}