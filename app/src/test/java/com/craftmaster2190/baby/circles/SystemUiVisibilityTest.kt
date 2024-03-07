package com.craftmaster2190.baby.circles

import android.view.View
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.Modifier

class SystemUiVisibilityTest {
    @Test
    fun test() {
        //Settings used in Baby Tap
//        setSystemUiVisibility(772);
//        setSystemUiVisibility(1280);
//        setSystemUiVisibility(2823);

        val systemUiFields = View::class.java.declaredFields
            .filter { field ->
                Modifier.isStatic(field.modifiers)
                        && field.type == Int::class.java
                        && field.name.startsWith("SYSTEM_UI_FLAG_")
            }
            .associate { field -> field.name to field.getInt(null) }

        for (value in arrayOf(772, 1280, 2823)) {
            systemUiFields.forEach { name, flag ->
                val matchesFlag = value and flag == flag
                if (matchesFlag) {
                    println("Flag $name & $value => $matchesFlag")
                }
            }
        }

        @Suppress("DEPRECATION")
        assertEquals(
            View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_VISIBLE, 772
        )

        @Suppress("DEPRECATION")
        assertEquals(
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_VISIBLE, 1280
        )

        @Suppress("DEPRECATION")
        assertEquals(
            View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_VISIBLE, 2823
        )
    }
}