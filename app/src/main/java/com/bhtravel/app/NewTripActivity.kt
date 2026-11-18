
package com.bhtravel.app

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewTripActivity : AppCompatActivity() {

    private lateinit var etDestination: EditText
    private lateinit var etCountry: EditText
    private lateinit var etTravelers: EditText
    private lateinit var etNotes: EditText

    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button

    private var startDate: Calendar? = null
    private var endDate: Calendar? = null

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)

        etDestination = findViewById(R.id.etDestination)
        etCountry = findViewById(R.id.etCountry)
        etTravelers = findViewById(R.id.etTravelers)
        etNotes = findViewById(R.id.etNotes)

        btnStartDate = findViewById(R.id.btnStartDate)
        btnEndDate = findViewById(R.id.btnEndDate)

        val btnSaveTrip = findViewById<Button>(R.id.btnSaveTrip)
        val btnCancelTrip = findViewById<Button>(R.id.btnCancelTrip)

        btnStartDate.setOnClickListener {
            showDatePicker(isStartDate = true)
        }

        btnEndDate.setOnClickListener {
            showDatePicker(isStartDate = false)
        }

        btnSaveTrip.setOnClickListener {
            validateTrip()
        }

        btnCancelTrip.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val selectedDate = if (isStartDate) startDate else endDate
        val calendar = (selectedDate ?: Calendar.getInstance()).clone() as Calendar

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                val chosenDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if (isStartDate) {
                    startDate = chosenDate
                    btnStartDate.text = dateFormat.format(chosenDate.time)

                    if (endDate != null && endDate!!.before(chosenDate)) {
                        endDate = null
                        btnEndDate.text = "Choose end date"
                    }
                } else {
                    val chosenStartDate = startDate

                    if (chosenStartDate != null && chosenDate.before(chosenStartDate)) {
                        Toast.makeText(
                            this,
                            "End date cannot be before start date.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        endDate = chosenDate
                        btnEndDate.text = dateFormat.format(chosenDate.time)
                    }
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        if (!isStartDate && startDate != null) {
            datePicker.datePicker.minDate = startDate!!.timeInMillis
        }

        datePicker.show()
    }

    private fun validateTrip() {
        val destination = etDestination.text.toString().trim()
        val country = etCountry.text.toString().trim()
        val travelersText = etTravelers.text.toString().trim()
        val notes = etNotes.text.toString().trim()

        if (destination.isEmpty()) {
            etDestination.error = "Please enter a destination"
            etDestination.requestFocus()
            return
        }

        if (country.isEmpty()) {
            etCountry.error = "Please enter a country"
            etCountry.requestFocus()
            return
        }

        if (startDate == null) {
            Toast.makeText(this, "Please choose a start date.", Toast.LENGTH_SHORT).show()
            return
        }

        if (endDate == null) {
            Toast.makeText(this, "Please choose an end date.", Toast.LENGTH_SHORT).show()
            return
        }

        if (endDate!!.before(startDate!!)) {
            Toast.makeText(
                this,
                "End date cannot be before start date.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val travelers = travelersText.toIntOrNull()

        if (travelers == null || travelers < 1) {
            etTravelers.error = "Enter at least 1 traveler"
            etTravelers.requestFocus()
            return
        }

        val formattedStartDate = dateFormat.format(startDate!!.time)
        val formattedEndDate = dateFormat.format(endDate!!.time)

        Toast.makeText(
            this,
            "Trip details are valid! $destination, $country\n" +
                    "$formattedStartDate - $formattedEndDate\n" +
                    "Travelers: $travelers",
            Toast.LENGTH_LONG
        ).show()

        // The trip will be saved permanently after we add the Room database.
    }
}
