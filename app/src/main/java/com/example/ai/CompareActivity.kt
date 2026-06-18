package com.example.kotlin

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView

class CompareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compareactivity)

        // 🌊 ROOT VIEW
        val root = findViewById<View>(android.R.id.content)

        //  Gradient background(the same one in main )
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.parseColor("#081B1B"),
                Color.parseColor("#0F2A2A"),
                Color.parseColor("#163A33")
            )
        )

        root.background = gradient

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

        //  ANIMATION BACKGROUND
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2500L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener {
                val v = it.animatedValue as Float

                gradient.colors = intArrayOf(
                    blend(colors1[0], colors2[0], v),
                    blend(colors1[1], colors2[1], v),
                    blend(colors1[2], colors2[2], v)
                )

                root.background = gradient
            }
        }.start()

        val spinnerA = findViewById<Spinner>(R.id.spinnerA)
        val spinnerB = findViewById<Spinner>(R.id.spinnerB)
        val spinnerMetric = findViewById<Spinner>(R.id.spinnerMetric)
        val btn = findViewById<Button>(R.id.btnCompare)
        val chart = findViewById<BarChart>(R.id.chart)

        val aiModels = arrayOf(
            "🤖 GPT-5.2", "🤖 GPT-5.3", "🤖 GPT-5.4",
            "🧠 Haiku 4.6", "🎭 Sonnet 4.6", "🔥 Opus 4.6",
            "⚡ Gemini 2.5 Flash", "🚀 Gemini 2.5 Pro", "🌌 Gemini 3.1 Pro"
        )

        val metrics = arrayOf("⚡ Energy", "💧 Water", "🌍 CO2")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, aiModels)
        val metricAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, metrics)

        spinnerA.adapter = adapter
        spinnerB.adapter = adapter
        spinnerMetric.adapter = metricAdapter

        btn.setOnClickListener {

            val aiA = spinnerA.selectedItem.toString()
            val aiB = spinnerB.selectedItem.toString()
            val metric = spinnerMetric.selectedItem.toString()

            val v1 = getValue(aiA, metric).toFloat()
            val v2 = getValue(aiB, metric).toFloat()

            val entries = listOf(
                BarEntry(0f, v1),
                BarEntry(1f, v2)
            )

            val dataSet = BarDataSet(entries, metric).apply {
                colors = listOf(
                    Color.parseColor("#5A8F76"),
                    Color.parseColor("#E9C46A")
                )
                valueTextColor = Color.WHITE
                valueTextSize = 14f
            }

            val data = BarData(dataSet).apply {
                barWidth = 0.3f   //  باریک‌تر شدن ستون‌ها
            }

            chart.data = data
            chart.description.isEnabled = false
            chart.legend.isEnabled = false


            chart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(listOf(aiA, aiB))
                textColor = Color.WHITE
                setDrawGridLines(false)
                granularity = 1f
                setLabelCount(2, true)
            }

            //   (شروع از صفر)
            chart.axisLeft.apply {
                textColor = Color.WHITE
                axisMinimum = 0f
            }

            chart.axisRight.isEnabled = false

            chart.animateY(900)
            chart.invalidate()
        }

        // Bottom Nav
        val nav = findViewById<BottomNavigationView>(R.id.bottomNav)
        nav.selectedItemId = R.id.nav_compare

        nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_map -> startActivity(Intent(this, MapActivity::class.java))
                R.id.nav_calc -> startActivity(Intent(this, CalculatorActivity::class.java))
                R.id.nav_about -> startActivity(Intent(this, AboutActivity::class.java))
            }
            true
        }
    }

    //  Blend
    private fun blend(c1: Int, c2: Int, ratio: Float): Int {
        val r = (Color.red(c1) * (1 - ratio) + Color.red(c2) * ratio).toInt()
        val g = (Color.green(c1) * (1 - ratio) + Color.green(c2) * ratio).toInt()
        val b = (Color.blue(c1) * (1 - ratio) + Color.blue(c2) * ratio).toInt()
        return Color.rgb(r, g, b)
    }

    private fun getValue(ai: String, metric: String): Double {

        val (energy, water, co2) = when (ai) {

            "🤖 GPT-5.2" -> Triple(0.48, 0.32, 0.40)
            "🤖 GPT-5.3" -> Triple(0.45, 0.30, 0.37)
            "🤖 GPT-5.4" -> Triple(0.42, 0.28, 0.33)

            "🧠 Haiku 4.6" -> Triple(0.38, 0.25, 0.30)
            "🎭 Sonnet 4.6" -> Triple(0.41, 0.27, 0.34)
            "🔥 Opus 4.6" -> Triple(0.50, 0.34, 0.42)

            "⚡ Gemini 2.5 Flash" -> Triple(0.35, 0.22, 0.25)
            "🚀 Gemini 2.5 Pro" -> Triple(0.40, 0.26, 0.32)
            "🌌 Gemini 3.1 Pro" -> Triple(0.55, 0.36, 0.45)

            else -> Triple(0.45, 0.30, 0.40)
        }

        return when (metric) {
            "⚡ Energy" -> energy
            "💧 Water" -> water
            "🌍 CO2" -> co2
            else -> energy
        }
        //// back ground animation is not working for some reason just fic it
    }
}