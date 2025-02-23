package com.example.mortalminion

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import java.time.format.DateTimeFormatter
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.widget.ProgressBar
import android.util.Log

class MainActivity : AppCompatActivity() {
    
    private lateinit var datePickerCard: MaterialCardView
    private lateinit var selectDateButton: Button
    private lateinit var confirmDateButton: Button
    private lateinit var selectedDateText: TextView
    private lateinit var statsContainer: MaterialCardView
    private lateinit var lifeProgressBar: LinearProgressIndicator
    private lateinit var percentageTextView: TextView
    private lateinit var daysLeftTextView: TextView
    private lateinit var yearsValue: TextView
    private lateinit var daysValue: TextView
    private lateinit var hoursValue: TextView
    private lateinit var birthDateDisplay: TextView
    private lateinit var editDateButton: Button
    private lateinit var lifeExpectancyDisplay: TextView
    private lateinit var editExpectancyButton: Button
    private lateinit var yearsTotal: TextView
    private lateinit var daysTotal: TextView
    private lateinit var hoursTotal: TextView
    private lateinit var yearsProgress: ProgressBar
    private lateinit var daysProgress: ProgressBar
    private lateinit var hoursProgress: ProgressBar
    
    private val defaultLifeExpectancy = 75
    private lateinit var datePreferences: DatePreferences
    private var selectedDate: LocalDate? = null
    private var lifeExpectancy = 75
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Starting onCreate")
            setContentView(R.layout.activity_main)
            
            datePreferences = DatePreferences(this)
            Log.d(TAG, "DatePreferences initialized")
            
            initializeViews()
            Log.d(TAG, "Views initialized")
            
            setupDateSelection()
            Log.d(TAG, "Date selection setup complete")
            
            loadSavedBirthDate()
            Log.d(TAG, "Saved birth date loaded")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
        }
    }

    private fun initializeViews() {
        try {
            datePickerCard = findViewById(R.id.datePickerCard)
            selectDateButton = findViewById(R.id.selectDateButton)
            confirmDateButton = findViewById(R.id.confirmDateButton)
            selectedDateText = findViewById(R.id.selectedDateText)
            statsContainer = findViewById(R.id.statsContainer)
            lifeProgressBar = findViewById(R.id.lifeProgressBar)
            percentageTextView = findViewById(R.id.percentageTextView)
            daysLeftTextView = findViewById(R.id.daysLeftTextView)
            yearsValue = findViewById(R.id.yearsValue)
            daysValue = findViewById(R.id.daysValue)
            hoursValue = findViewById(R.id.hoursValue)
            birthDateDisplay = findViewById(R.id.birthDateDisplay)
            editDateButton = findViewById(R.id.editDateButton)
            lifeExpectancyDisplay = findViewById(R.id.lifeExpectancyDisplay)
            editExpectancyButton = findViewById(R.id.editExpectancyButton)
            yearsTotal = findViewById(R.id.yearsTotal)
            daysTotal = findViewById(R.id.daysTotal)
            hoursTotal = findViewById(R.id.hoursTotal)
            yearsProgress = findViewById(R.id.yearsProgress)
            daysProgress = findViewById(R.id.daysProgress)
            hoursProgress = findViewById(R.id.hoursProgress)

            lifeProgressBar.max = 100
            statsContainer.visibility = View.GONE
            confirmDateButton.visibility = View.GONE

            setupEditButton()
            setupEditExpectancyButton()
            updateLifeExpectancyDisplay()

            yearsProgress.max = 100
            daysProgress.max = 100
            hoursProgress.max = 100
            Log.d(TAG, "All views found successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing views", e)
        }
    }

    private fun loadSavedBirthDate() {
        lifecycleScope.launch {
            try {
                val savedDate = datePreferences.birthDate.firstOrNull()
                Log.d(TAG, "Saved date: $savedDate")
                if (savedDate != null) {
                    selectedDate = savedDate
                    datePickerCard.visibility = View.GONE
                    statsContainer.visibility = View.VISIBLE
                    updateAgeCalculations(savedDate)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading saved birth date", e)
            }
        }
    }

    private fun setupDateSelection() {
        selectDateButton.setOnClickListener {
            showDatePicker()
        }

        confirmDateButton.setOnClickListener {
            selectedDate?.let { date ->
                lifecycleScope.launch {
                    datePreferences.saveBirthDate(date)
                    datePickerCard.visibility = View.GONE
                    statsContainer.visibility = View.VISIBLE
                    updateAgeCalculations(date)
                }
            }
        }
    }

    private fun showDatePicker() {
        val today = LocalDate.now()
        val dialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
                selectedDateText.text = "Selected: ${selectedDate.toString()}"
                selectedDateText.visibility = View.VISIBLE
                confirmDateButton.visibility = View.VISIBLE
            },
            today.year - 20,
            today.monthValue - 1,
            today.dayOfMonth
        )
        
        dialog.datePicker.maxDate = System.currentTimeMillis()
        val minDate = LocalDate.now().minusYears(120)
        dialog.datePicker.minDate = minDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        
        dialog.show()
    }

    private fun setupEditButton() {
        editDateButton.setOnClickListener {
            datePickerCard.visibility = View.VISIBLE
            statsContainer.visibility = View.GONE
            selectedDateText.visibility = View.GONE
            confirmDateButton.visibility = View.GONE
        }
    }

    private fun setupEditExpectancyButton() {
        editExpectancyButton.setOnClickListener {
            showLifeExpectancyDialog()
        }
    }

    private fun showLifeExpectancyDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText(lifeExpectancy.toString())

        AlertDialog.Builder(this)
            .setTitle("Set Life Expectancy")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val newValue = input.text.toString().toIntOrNull()
                if (newValue != null && newValue > 0) {
                    lifeExpectancy = newValue
                    updateLifeExpectancyDisplay()
                    selectedDate?.let { updateAgeCalculations(it) }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateLifeExpectancyDisplay() {
        lifeExpectancyDisplay.text = "Life Expectancy: $lifeExpectancy years"
    }

    private fun updateAgeCalculations(birthDate: LocalDate) {
        val today = LocalDate.now()
        selectedDate = birthDate
        
        val age = Period.between(birthDate, today)
        val totalDays = ChronoUnit.DAYS.between(birthDate, today)
        val totalHours = ChronoUnit.HOURS.between(
            birthDate.atStartOfDay(),
            today.atStartOfDay().plusDays(1)
        )

        // Calculate totals
        val totalExpectedYears = lifeExpectancy
        val totalExpectedDays = (lifeExpectancy * 365.25).roundToInt()
        val totalExpectedHours = totalExpectedDays * 24L

        // Calculate life percentage and days left
        val daysLeft = totalExpectedDays - totalDays
        val percentageLived = ((totalDays.toDouble() / totalExpectedDays) * 100).roundToInt()
            .coerceIn(0, 100)

        // Calculate progress percentages
        val yearsPercentage = (age.years.toDouble() / totalExpectedYears * 100).roundToInt()
        val daysPercentage = (totalDays.toDouble() / totalExpectedDays * 100).roundToInt()
        val hoursPercentage = (totalHours.toDouble() / totalExpectedHours * 100).roundToInt()

        // Update UI with formatted numbers
        yearsValue.text = "${age.years}"
        daysValue.text = "%,d".format(totalDays)
        hoursValue.text = "%,d".format(totalHours)
        
        yearsTotal.text = "of $totalExpectedYears years"
        daysTotal.text = "of %,d days".format(totalExpectedDays)
        hoursTotal.text = "of %,d hours".format(totalExpectedHours)
        
        percentageTextView.text = "$percentageLived%"
        lifeProgressBar.progress = percentageLived
        daysLeftTextView.text = "Approximately %,d days left to live".format(daysLeft)
        birthDateDisplay.text = "Birth Date: ${birthDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}"

        // Update progress bars
        yearsProgress.progress = yearsPercentage.coerceIn(0, 100)
        daysProgress.progress = daysPercentage.coerceIn(0, 100)
        hoursProgress.progress = hoursPercentage.coerceIn(0, 100)
    }
}