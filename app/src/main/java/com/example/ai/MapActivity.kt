package com.example.kotlin

import android.content.Intent
import android.graphics.Color// not using it but dont delet it
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val markers = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.selectedItemId = R.id.nav_map

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_calc -> startActivity(Intent(this, CalculatorActivity::class.java))
                R.id.nav_compare -> startActivity(Intent(this, CompareActivity::class.java))
                R.id.nav_about -> startActivity(Intent(this, AboutActivity::class.java))
            }
            true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
            isScrollGesturesEnabled = true
            isRotateGesturesEnabled = true
        }

        val aiModels = arrayOf("GPT", "Claude", "Gemini")
        val spinnerAI = findViewById<Spinner>(R.id.spinnerMapAI)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            aiModels
        )

        spinnerAI.adapter = adapter


        // SERVER LISTS(do it with manu)


        val gptServers = listOf(
            Triple("OpenAI - Abilene, Texas", 32.4487, -99.7331),
            Triple("OpenAI - New Mexico", 32.3490, -106.8320),
            Triple("OpenAI - Michigan", 42.1833, -83.9833),
            Triple("OpenAI - Abu Dhabi", 24.4539, 54.3773),
            Triple("OpenAI - Narvik", 68.4380, 17.4270),

            Triple("GPT - Virginia", 37.4316, -78.6569),
            Triple("GPT - California", 37.4220, -122.0841),
            Triple("GPT - London", 51.5072, -0.1276),
            Triple("GPT - Tokyo", 35.6762, 139.6503),
            Triple("GPT - Sydney", -33.8688, 151.2093)
        )

        val claudeServers = listOf(
            Triple("Anthropic - Texas", 31.9686, -99.9018),
            Triple("Anthropic - New York", 40.7128, -74.0060),
            Triple("Anthropic - Indiana", 40.2672, -86.1349),

            Triple("Claude - Oregon", 45.5152, -122.6784),
            Triple("Claude - Frankfurt", 50.1109, 8.6821),
            Triple("Claude - Singapore", 1.3521, 103.8198),
            Triple("Claude - Seoul", 37.5665, 126.9780),
            Triple("Claude - Mumbai", 19.0760, 72.8777)
        )

        val geminiServers = listOf(
            Triple("Google - Iowa", 41.2619, -95.8608),
            Triple("Google - South Carolina", 33.1960, -79.9920),
            Triple("Google - Georgia", 33.7515, -84.7477),
            Triple("Google - Oklahoma", 36.3015, -95.2363),
            Triple("Google - Tennessee", 36.5298, -87.3595),
            Triple("Google - Virginia", 39.0438, -77.4874),
            Triple("Google - Oregon", 45.5946, -121.1787),
            Triple("Google - Belgium", 50.4480, 3.8180),
            Triple("Google - Netherlands", 53.4490, 6.8560),
            Triple("Google - Finland", 60.5697, 27.1979),
            Triple("Google - Ireland", 53.3498, -6.2603),
            Triple("Google - London", 51.5072, -0.1276),
            Triple("Google - Tokyo", 35.6762, 139.6503),
            Triple("Google - Singapore", 1.3521, 103.8198),
            Triple("Google - Sydney", -33.8688, 151.2093)
        )


        // SHOW FUNCTION


        fun showServers(list: List<Triple<String, Double, Double>>) {

            markers.forEach { it.remove() }
            markers.clear()

            list.forEach {
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.second, it.third))
                        .title(it.first)
                )
                marker?.let { m -> markers.add(m) }
            }

            val first = list.firstOrNull()
            first?.let {
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.second, it.third),
                        3.5f
                    )
                )
            }
        }

        // default
        showServers(gptServers)

        spinnerAI.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (aiModels[position]) {
                        "GPT" -> showServers(gptServers)
                        "Claude" -> showServers(claudeServers)
                        "Gemini" -> showServers(geminiServers)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }
}