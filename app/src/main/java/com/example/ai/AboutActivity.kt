package com.example.kotlin

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_about)

        // 🌊 ROOT VIEW (کل صفحه)
        val layout = findViewById<View>(R.id.rootLayout)

        // 🎨 Gradient Background
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

        // 🌊 Animation
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2500L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener {
                val value = it.animatedValue as Float

                gradient.colors = intArrayOf(
                    blend(colors1[0], colors2[0], value),
                    blend(colors1[1], colors2[1], value),
                    blend(colors1[2], colors2[2], value)
                )

                layout.background = gradient
            }
        }

        animator.start()

        //  Back Button
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        //  Bottom Navigation
        val nav = findViewById<BottomNavigationView>(R.id.bottomNav)

        nav.selectedItemId = R.id.nav_about

        nav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }

                R.id.nav_map -> {
                    startActivity(Intent(this, MapActivity::class.java))
                }

                R.id.nav_calc -> {
                    startActivity(Intent(this, CalculatorActivity::class.java))
                }

                R.id.nav_compare -> {
                    startActivity(Intent(this, CompareActivity::class.java))
                }

                R.id.nav_about -> {
                }
            }

            true
        }

        //  Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(layout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    //  Color blend function
    private fun blend(c1: Int, c2: Int, ratio: Float): Int {
        val r = (Color.red(c1) * (1 - ratio) + Color.red(c2) * ratio).toInt()
        val g = (Color.green(c1) * (1 - ratio) + Color.green(c2) * ratio).toInt()
        val b = (Color.blue(c1) * (1 - ratio) + Color.blue(c2) * ratio).toInt()

        return Color.rgb(r, g, b)
    }
}