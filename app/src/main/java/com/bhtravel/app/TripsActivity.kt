
package com.bhtravel.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
            content.addView(createDetailText("📝  ${trip.notes}"))
        }

        // Edit and Delete buttons
        val buttonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, dp(12), 0, 0)
        }

        val editButton = Button(this).apply {
            text = "Edit"
            setTextColor(0xFFFFFFFF.toInt())
            setBackgroundColor(0xFF159A9C.toInt())

            setOnClickListener {
                openEditTrip(trip)
            }

            layoutParams = LinearLayout.LayoutParams(
                0,
                dp(48),
                1f
            ).apply {
                marginEnd = dp(8)
            }
        }

        val deleteButton = Button(this).apply {
            text = "Delete"
            setTextColor(0xFFFFFFFF.toInt())
            setBackgroundColor(0xFFD9534F.toInt())

            setOnClickListener {
                showDeleteConfirmation(trip)
            }

            layoutParams = LinearLayout.LayoutParams(
                0,
                dp(48),
                1f
            ).apply {
                marginStart = dp(8)
            }
        }

        buttonRow.addView(editButton)
        buttonRow.addView(deleteButton)
        content.addView(buttonRow)

        // Open in Maps button
        val mapsButton = Button(this).apply {
            text = "📍  Open in Maps"
            setTextColor(0xFFFFFFFF.toInt())
            setBackgroundColor(0xFF17324D.toInt())

            setOnClickListener {
                openInMaps(trip)
            }

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(48)
            ).apply {
                topMargin = dp(8)
            }
        }

        content.addView(mapsButton)

        card.addView(content)

        return card
    }

    private fun openEditTrip(trip: TripEntity) {
        val intent = Intent(this, NewTripActivity::class.java)
        intent.putExtra(NewTripActivity.EXTRA_TRIP_ID, trip.id)
        startActivity(intent)
    }

    // Open Google Maps through the browser
    private fun openInMaps(trip: TripEntity) {
        val location = "${trip.destination}, ${trip.country}"

        val mapsUrl =
            "https://www.google.com/maps/search/?api=1&query=${Uri.encode(location)}"

        val mapIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(mapsUrl)
        )

        startActivity(mapIntent)
    }

    private fun showDeleteConfirmation(trip: TripEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete trip")
            .setMessage(
                "Are you sure you want to delete ${trip.destination}?"
            )
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                tripViewModel.deleteTrip(trip)
            }
            .show()
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
