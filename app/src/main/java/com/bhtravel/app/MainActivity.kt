
package com.bhtravel.app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNewTrip = findViewById<TextView>(R.id.btnNewTrip)

        btnNewTrip.setOnClickListener {
            startActivity(Intent(this, NewTripActivity::class.java))
        }

        val navTrips = findViewById<TextView>(R.id.navTrips)

        navTrips.setOnClickListener {
            startActivity(Intent(this, TripsActivity::class.java))
        }
    }
}
