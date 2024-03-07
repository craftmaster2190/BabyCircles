package com.craftmaster2190.baby.circles

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.security.SecureRandom
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.random.Random

class MovingBallView(context: Context) : View(context) {

    private var scheduledFuture: ScheduledFuture<*>? = null
    private val balls = ConcurrentLinkedDeque<Ball>()
    private var isRunning = false // resume is called to init this
    private val scheduledExecutor by lazy {
        Executors.newSingleThreadScheduledExecutor { runnable ->
            Thread(runnable).apply {
                isDaemon = true
                name = MovingBallView::class.java.simpleName + "-animate"
            }
        }
    }
    private val random by lazy {
        Random(SecureRandom().nextLong())
    }
    private var lastBallDied = AtomicReference<Long>()

    init {
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                balls.offer(randomBall().apply {
                    x = event.x
                    y = event.y
                })
                lastBallDied.set(null)
            }
            true
        }

        post {
            balls.offer(randomBall())
            postInvalidate()
            resume()
        }
    }


    private fun randomBall() = Ball(
        color = randomColor(),
        x = randomFloat(width),
        y = randomFloat(height),
        radius = randomFloat(maxBallSize()),
        shrinkRate = randomFloat(1f),
        velocity = randomFloat(maxBallVelocity()),
        direction = randomAngleRadians()
    )

    private val colors = arrayListOf(
        Color.RED,
        Color.CYAN,
        Color.BLUE,
        Color.MAGENTA,
        Color.YELLOW,
        Color.GREEN,
        Color.GRAY,
        Color.parseColor("#7F27FF"), // Violet
        Color.parseColor("#FF8911"), // Orange
        Color.parseColor("#74E291"), // Green
        Color.parseColor("#40A2E3"), // Blue
        Color.parseColor("#D63484"), // Pink
        Color.parseColor("#DCFFB7"), // Lime
        Color.parseColor("#3559E0"), // Blue
        Color.parseColor("#E0F4FF"), // LightBlue
        Color.parseColor("#FF6C22"), // Orange
        Color.parseColor("#B15EFF"), // Purple
        Color.parseColor("#FF4B91"), // Pink
        Color.parseColor("#C70039"), // Red
        Color.parseColor("#ECEE81"), // Sprite
    )

    private fun randomColor(): Int {
        return colors.random()
    }

    private fun randomAngleRadians() = Math.toRadians(randomFloat(360f).toDouble()).toFloat()

    fun pause() {
        Log.i(MovingBallView::class.java.simpleName, "pause")
        isRunning = false
        scheduledFuture?.cancel(false)
        scheduledFuture = null
    }

    fun resume() {
        if (!isRunning) {
            Log.i(MovingBallView::class.java.simpleName, "resume")
            isRunning = true
            if (scheduledFuture == null) {
                scheduledFuture = scheduledExecutor.scheduleWithFixedDelay({
                    if (isRunning) {
                        progressBalls()
                        generateBallIfNone()
                        postInvalidate()
                    }
                }, 0, 1000 / 60, TimeUnit.MILLISECONDS)
            }
        }
    }

    private fun generateBallIfNone() {
        if (balls.isEmpty()) {
            val lastBallDied_ = lastBallDied.get()
            if (lastBallDied_ != null && lastBallDied_ + 3000L < System.currentTimeMillis()) {
                Log.i("generateBallIfNone", "adding ball")
                balls.offer(randomBall())
                lastBallDied.set(null)
            } else {
                Log.i(
                    "generateBallIfNone",
                    "not ready to add ball timeSinceLastBallDied=${System.currentTimeMillis() - lastBallDied_}"
                )
            }
        }
    }

    private fun progressBalls() {
        balls.forEach { ball ->
            ball.move()
            ball.shrink()
            if (ball.x < 0 || ball.x > width || ball.y < 0 || ball.y > height) {
                redirectBall(ball)
            }
        }
        balls.removeIf { ball -> ball.radius <= 0 }
        if (balls.isEmpty() && lastBallDied.get() == null) {
            Log.i("progressBalls", "lastBallDied")
            lastBallDied.set(System.currentTimeMillis())
        }
    }

    private fun redirectBall(ball: Ball) {
        var randomPointOnScreenX = randomFloat(width)
        var randomPointOnScreenY = randomFloat(height)

        val initialDirection = ball.getDirection()
        // TODO Is direction towards screen
        ball.setDirectionTowards(randomPointOnScreenX, randomPointOnScreenY)
        Log.i("redirectBall", "redirected from initialDirection=$initialDirection -> $ball")
    }

    private fun maxBallSize(): Float {
        return Math.min(width, height) / 3f
    }

    private fun maxBallVelocity(): Float {
        return Math.min(width, height) / 17f
    }

    private fun randomFloat(upperBound: Int): Float {
        return random.nextDouble(upperBound.toDouble()).toFloat()
    }

    private fun randomFloat(upperBound: Float): Float {
        return random.nextDouble(upperBound.toDouble()).toFloat()
    }

    private val paint by lazy { Paint().apply { style = Paint.Style.FILL } }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        balls.forEach { ball ->
            paint.color = ball.color
            canvas.drawCircle(ball.x, ball.y, ball.radius, paint)
            Log.v("onDraw", "Paint $ball")
        }
    }
}