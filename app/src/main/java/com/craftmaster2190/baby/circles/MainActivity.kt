package com.craftmaster2190.baby.circles

import android.app.ActivityManager
import android.app.ActivityManager.LOCK_TASK_MODE_NONE
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    private val activityManager by lazy {
        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    private val movingBallView by lazy {
        MovingBallView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(movingBallView)
    }

    override fun onResume() {
        super.onResume()
        tryStartChildLock()
        movingBallView.resume()
    }

    override fun onPause() {
        super.onPause()
        movingBallView.pause()
    }

    private fun tryStartChildLock() {
        if (isChildLocked()) {
            startChildLock()
        }
    }

    private fun isChildLocked() = activityManager.lockTaskModeState == LOCK_TASK_MODE_NONE

    fun startChildLock() {
        Log.i("ChildLock", "startChildLock")
        @Suppress("DEPRECATION")
        this.findViewById<View>(android.R.id.content).systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN and
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION and
                    View.SYSTEM_UI_FLAG_IMMERSIVE and
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION and
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE and
                    View.SYSTEM_UI_FLAG_LOW_PROFILE and
                    View.SYSTEM_UI_FLAG_VISIBLE

        startLockTask()
    }
}
