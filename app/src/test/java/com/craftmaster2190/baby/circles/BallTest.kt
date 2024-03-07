package com.craftmaster2190.baby.circles

import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BallTest {
    @Test
    fun moveTest() {
        val ball = Ball(0, 1f, 1f, 1f, 0f, 1f, 0f)
        ball.move()
        assertThat("x", ball.x, `is`(2f))
        assertThat("y", ball.y, `is`(1f))
        assertThat("directionIsRightString", ball.directionString(), containsString("right"))

        ball.setDirection((Math.PI * .5f).toFloat())
        ball.move()
        assertThat("x", ball.x, `is`(2f))
        assertThat("y", ball.y, `is`(2f))
        assertThat("directionIsDownString", ball.directionString(), containsString("down"))
    }

    @Test
    fun isInRectTest() {
        val ball = Ball(0, 1f, 1f, 1f, 0f, 1f, 0f)
        val bounds = ImmutableRectangle.from(0f, 0f, 50f, 50f)

        assertEquals(bounds.right, 50f)

        assertTrue("inBounds", ball.isInRect(bounds))

        ball.x = 0f
        assertFalse("outOfBounds-xOnLeftLine", ball.isInRect(bounds))
        ball.x = -1f
        assertFalse("outOfBounds-xPastLeftLine", ball.isInRect(bounds))
        ball.x = 51f
        assertFalse("outOfBounds-xPastRightLine", ball.isInRect(bounds))
        ball.x = 1f
        assertTrue("inBounds", ball.isInRect(bounds))

        ball.y = 0f
        assertFalse("outOfBounds-yOnLeftLine", ball.isInRect(bounds))
        ball.y = -1f
        assertFalse("outOfBounds-yPastLeftLine", ball.isInRect(bounds))
        ball.y = 51f
        assertFalse("outOfBounds-yPastRightLine", ball.isInRect(bounds))
        ball.y = 1f
        assertTrue("inBounds", ball.isInRect(bounds))
    }

    @Test
    fun isDirectionTowardsTest() {
        val ball = Ball(0, -10f, -10f, 1f, 0f, 1f, 0f)
        val bounds = ImmutableRectangle.from(0f, 0f, 50f, 50f)

        ball.setDirectionTowards(bounds.right, bounds.bottom)
        assertThat("directionTowardsBottomRightString", ball.directionString(), `is`("down right"))
        assertThat("directionTowardsBottomRight", ball.getDirection(), IsCloseTo(0.7f, 0.1f))
    }

}