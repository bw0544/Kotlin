package com.example.kotlin

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val layout = findViewById<View>(R.id.main)

        // background(idk abt the colors)
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#081B1B"),
                Color.parseColor("#0F2A2A"),
                Color.parseColor("#163A33")
            )
        )

        layout.background = gradient

        val colors1 = intArrayOf(
            Color.parseColor("#081B1B"),
            Color.parseColor("#0F2A2A"),
            Color.parseColor("#163A33")
        )

        val colors2 = intArrayOf(
            Color.parseColor("#0F2A2A"),
            Color.parseColor("#163A33"),
            Color.parseColor("#96CDB0")
        )

        // animated background
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2500L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener {
                val value = it.animatedValue as Float

                val newColors = intArrayOf(
                    blendColorInt(colors1[0], colors2[0], value),
                    blendColorInt(colors1[1], colors2[1], value),
                    blendColorInt(colors1[2], colors2[2], value)
                )

                gradient.colors = newColors
                layout.background = gradient
            }
        }
        animator.start()

        //Buttons(go to layout to check them )
        val btnMap = findViewById<Button>(R.id.btnMap)
        val btnCalc = findViewById<Button>(R.id.btnCalc)
        val btnCompare = findViewById<Button>(R.id.btnCompare)
        val btnInfo = findViewById<Button>(R.id.btnInfo)

        // press effect(fix it it doesnt work properly )
        fun Button.addPressEffect() {
            setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.animate()
                            .scaleX(0.96f)
                            .scaleY(0.96f)
                            .setDuration(80)
                            .start()
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(120)
                            .start()
                    }
                }
                false
            }
        }

        btnMap.addPressEffect()
        btnCalc.addPressEffect()
        btnCompare.addPressEffect()
        btnInfo.addPressEffect()


        btnMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }


        btnCalc.setOnClickListener {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }


        btnCompare.setOnClickListener {
            startActivity(Intent(this, CompareActivity::class.java))
        }


        btnInfo.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        // navigation
        val nav = findViewById<BottomNavigationView>(R.id.bottomNav)

        nav.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> true

                R.id.nav_map -> {
                    startActivity(Intent(this, MapActivity::class.java))
                    true
                }

                R.id.nav_calc -> {
                    startActivity(Intent(this, CalculatorActivity::class.java))
                    true
                }

                R.id.nav_compare -> {
                    startActivity(Intent(this, CompareActivity::class.java))
                    true
                }

                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }

                else -> false
            }
        }

        // Edge-to-edge fix
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Color blending function
    private fun blendColorInt(c1: Int, c2: Int, ratio: Float): Int {
        val r = (Color.red(c1) * (1 - ratio) + Color.red(c2) * ratio).toInt()
        val g = (Color.green(c1) * (1 - ratio) + Color.green(c2) * ratio).toInt()
        val b = (Color.blue(c1) * (1 - ratio) + Color.blue(c2) * ratio).toInt()

        return Color.rgb(r, g, b)
    }
}