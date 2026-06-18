package com.example.kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val spinnerAI = findViewById<Spinner>(R.id.spinnerAI)
        val inputText = findViewById<EditText>(R.id.inputText)
        val inputRequests = findViewById<EditText>(R.id.inputRequests)
        val btnCalc = findViewById<Button>(R.id.btnCalculate)
        val result = findViewById<TextView>(R.id.txtResult)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val aiModels = arrayOf(
            "GPT-5.2","GPT-5.3","GPT-5.4",
            "Haiku 4.6","Sonnet 4.6","Opus 4.6",
            "Gemini 2.5 Flash","Gemini 2.5 Pro","Gemini 3.1 Pro"
        )

        spinnerAI.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            aiModels
        )

        btnCalc.setOnClickListener {

            val text = inputText.text.toString()
            val requests = inputRequests.text.toString().toIntOrNull() ?: 1
            val selectedAI = spinnerAI.selectedItem.toString()

            val charCount = text.length

            val (e, w, c) = when (selectedAI) {

                "GPT-5.2" -> Triple(0.00052,0.00021,0.00032)
                "GPT-5.3" -> Triple(0.00055,0.00023,0.00034)
                "GPT-5.4" -> Triple(0.00060,0.00026,0.00036)

                "Haiku 4.6" -> Triple(0.00040,0.00015,0.00025)
                "Sonnet 4.6" -> Triple(0.00048,0.00018,0.00028)
                "Opus 4.6" -> Triple(0.00065,0.00030,0.00042)

                "Gemini 2.5 Flash" -> Triple(0.00035,0.00012,0.00020)
                "Gemini 2.5 Pro" -> Triple(0.00050,0.00020,0.00030)
                "Gemini 3.1 Pro" -> Triple(0.00070,0.00030,0.00045)

                else -> Triple(0.0005,0.0002,0.0003)
            }

            val energy = charCount * requests * e   // kWh
            val water = charCount * requests * w    // ml
            val co2 = charCount * requests * c      // g

            result.text = """
⚡ Energy: ${"%.5f".format(energy)} kWh
💧 Water: ${"%.2f".format(water)} ml
🌿 CO₂: ${"%.2f".format(co2)} g
""".trimIndent()
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_map -> startActivity(Intent(this, MapActivity::class.java))
                R.id.nav_calc -> { /* همین صفحه */ }
                R.id.nav_about -> startActivity(Intent(this, AboutActivity::class.java))
            }
            true
        }
    }
}