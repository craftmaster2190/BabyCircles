package com.craftmaster2190.baby.circles

import android.graphics.Color
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates

data class Ball(
    var color: Int,
    var x: Float,
    var y: Float,
    var radius: Float,
    var shrinkRate: Float,
    var velocity: Float, // speed in pixels
    private var direction: Float  // in radians (number between 0 and 2π/6.283) (0 = right, .5π = down, π = left, 1.5π = up)
) {
    private var xDirection by Delegates.notNull<Float>()
    private var yDirection by Delegates.notNull<Float>()
    private var isDirectionDown by Delegates.notNull<Boolean>()
    private var isDirectionLeft by Delegates.notNull<Boolean>()
    private var isDirectionUp by Delegates.notNull<Boolean>()
    private var isDirectionRight by Delegates.notNull<Boolean>()

    init {
        setDirection(direction)
    }

    fun getDirection(): Float {
        return direction
    }

    fun setDirection(direction: Float) {
        this.direction = direction
        xDirection = (velocity * cos(direction.toDouble())).toFloat()
        yDirection = (velocity * sin(direction.toDouble())).toFloat()
        isDirectionDown = direction > 0 && direction <= Math.PI
        isDirectionLeft = direction > Math.PI * .5 && direction <= Math.PI * 1.5
        isDirectionUp = !isDirectionDown
        isDirectionRight = !isDirectionLeft
    }

    fun setDirectionTowards(pointX: Float, pointY: Float) {
        val vectorX = pointX - x
        val vectorY = pointY - y
        val slope = vectorY / vectorX
        val newDirection = atan(slope)
        setDirection(newDirection)
    }

    fun move() {
        x += xDirection
        y += yDirection
    }

    fun shrink() {
        radius -= shrinkRate
    }

    override fun toString(): String {
        return "Ball{($x, $y) direction=$direction ${directionString()} radius=$radius shrinkRate=$shrinkRate color=${
            Color.valueOf(
                color
            )
        } }"
    }

    fun directionString(): String {
        return mapOf(
            "up" to isDirectionUp,
            "down" to isDirectionDown,
            "left" to isDirectionLeft,
            "right" to isDirectionRight,
        ).filterValues { it }
            .keys.joinToString(" ")
    }

    fun isInRect(rect: ImmutableRectangle): Boolean {
        val isInLeft = x > rect.left
        val isInRight = x < rect.right
        val isInTop = y > rect.top
        val isInBottom = y < rect.bottom
        return isInLeft && isInRight && isInTop && isInBottom
    }
}