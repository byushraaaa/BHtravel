
package com.bhtravel.app

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bhtravel.app.data.database.DatabaseProvider
import com.bhtravel.app.data.entity.TripEntity
import com.bhtravel.app.data.repository.TripRepository
import com.bhtravel.app.viewmodel.TripViewModel
import com.bhtravel.app.viewmodel.TripViewModelFactory
import com.google.android.material.card.MaterialCardView

class TripsActivity : AppCompatActivity() {

    private lateinit var tripsContainer: LinearLayout
    private lateinit var tvEmptyState: TextView
    private lateinit var tripViewModel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        tripsContainer = findViewById(R.id.tripsContainer)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val database = DatabaseProvider.getDatabase(applicationContext)
        val repository = TripRepository(database.tripDao())
        val factory = TripViewModelFactory(repository)

        tripViewModel = ViewModelProvider(this, factory)
            .get(TripViewModel::class.java)

        tripViewModel.allTrips.observe(this) { trips ->
            displayTrips(trips)
        }
    }

    private fun displayTrips(trips: List<TripEntity>) {
        tripsContainer.removeAllViews()

        if (trips.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            return
        }

        tvEmptyState.visibility = View.GONE

        trips.forEach { trip ->
            tripsContainer.addView(createTripCard(trip))
        }
    }

    private fun createTripCard(trip: TripEntity): MaterialCardView {
        val card = MaterialCardView(this).apply {
            radius = dp(18).toFloat()
            cardElevation = dp(3).toFloat()
            setCardBackgroundColor(0xFFFFFFFF.toInt())
            strokeWidth = dp(1)
            strokeColor = 0xFFE5EAF0.toInt()

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = dp(16)
            layoutParams = params
        }

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(18), dp(20), dp(18))
        }

        val destination = TextView(this).apply {
            text = "✈  ${trip.destination}"
            textSize = 21f
            setTextColor(0xFF17324D.toInt())
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val country = TextView(this).apply {
            text = trip.country
            textSize = 15f
            setTextColor(0xFF159A9C.toInt())
            setPadding(0, dp(5), 0, dp(12))
        }

        val dates = createDetailText(
            "📅  ${trip.startDate} – ${trip.endDate}"
        )

        val travelers = createDetailText(
            "👥  ${trip.travelers} traveler(s)"
        )

        content.addView(destination)
        content.addView(country)
        content.addView(dates)
        content.addView(travelers)

        if (trip.notes.isNotBlank()) {
            content.addView(
                createDetailText("📝  ${trip.notes}")
            )
        }

        card.addView(content)
        return card
    }

    private fun createDetailText(value: String): TextView {
        return TextView(this).apply {
            text = value
            textSize = 14f
            setTextColor(0xFF526477.toInt())
            setPadding(0, dp(5), 0, dp(5))
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
