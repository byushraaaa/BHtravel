
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
            val intent = Intent(this, NewTripActivity::class.java)
            startActivity(intent)
        }
    }
}
