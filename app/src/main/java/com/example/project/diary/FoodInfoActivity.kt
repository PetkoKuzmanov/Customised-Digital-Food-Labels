package com.example.project.diary

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.project.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class FoodInfoActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    private var carbohydratesTotalPercent: Int? = null
    private var fatsTotalPercent: Int? = null
    private var proteinsTotalPercent: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_info)
        setSupportActionBar(findViewById(R.id.foodInfoToolbar))

        val foodNameTextView = findViewById<TextView>(R.id.foodNameTextView)
        val foodDescriptionTextView = findViewById<TextView>(R.id.foodDescriptionTextView)
        val numberOfServingsEditText = findViewById<EditText>(R.id.quickAddEditText)

        val foodAmount = intent.getStringExtra(getString(R.string.amount).toLowerCase()).toString().toInt()
        val caloriesAmount = intent.getStringExtra(getString(R.string.caloriesAmount)).toString().toInt()

        foodNameTextView.text = intent.getStringExtra(getString(R.string.name).toLowerCase())
        foodDescriptionTextView.text = intent.getStringExtra(getString(R.string.description).toLowerCase())
        numberOfServingsEditText.text =
            Editable.Factory.getInstance().newEditable(foodAmount.toString())

        val carbohydratesAmount = intent.getStringExtra(getString(R.string.carbohydratesAmount)).toString().toInt()
        val fatsAmount = intent.getStringExtra(getString(R.string.fatsAmount)).toString().toInt()
        val proteinsAmount = intent.getStringExtra(getString(R.string.proteinsAmount)).toString().toInt()

        carbohydratesTotalPercent =
            (((carbohydratesAmount * 4.0) / caloriesAmount) * 100).roundToInt()
        fatsTotalPercent = (((fatsAmount * 9.0) / caloriesAmount) * 100).roundToInt()
        proteinsTotalPercent = (((proteinsAmount * 4.0) / caloriesAmount) * 100).roundToInt()

        val pieChart = findViewById<PieChart>(R.id.foodInfoPieChart)
        setPieChart(pieChart, caloriesAmount)

        numberOfServingsEditText.doAfterTextChanged { editable ->
            if (editable?.isNotEmpty()!!) {
                updateMacronutrients(editable)
            } else {
                updateMacronutrientsToZero()
            }
        }

        updateMacronutrients(numberOfServingsEditText.text)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuString = intent.getStringExtra(getString(R.string.menu).toLowerCase())

        if (menuString == getString(R.string.addFood)) {
            menuInflater.inflate((R.menu.food_info_toolbar_layout), menu)
        } else if (menuString == getString(R.string.diary).toLowerCase()) {
            menuInflater.inflate((R.menu.food_edit_info_toolbar_layout), menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun foodInfoNext(item: MenuItem) {
        val numberOfServingsEditText = findViewById<EditText>(R.id.quickAddEditText)
        database = Firebase.database.reference

        val foodKey = intent.getStringExtra(getString(R.string.key).toLowerCase())
        val meal = intent.getStringExtra(getString(R.string.meal).toLowerCase()).toString()
        val id = intent.getStringExtra(getString(R.string.id).toLowerCase()).toString()
        val amount = numberOfServingsEditText.text.toString()
        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()

        val current = LocalDateTime.now()
        val formatterTime = DateTimeFormatter.ofPattern(getString(R.string.time_format))
        val formattedTime = current.format(formatterTime)

        val currentDateReference = mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid).child(getString(R.string.dates).toLowerCase()).child(currentDate).child(getString(R.string.diary).toLowerCase())
                .child(meal)
        }

        if (foodKey == null) {
            currentDateReference?.child(getString(R.string.food).toLowerCase() + "-$formattedTime")?.child(getString(R.string.id).toLowerCase())?.setValue(id)
            currentDateReference?.child(getString(R.string.food).toLowerCase() + "-$formattedTime")?.child(getString(R.string.amount).toLowerCase())?.setValue(amount)
        } else {
            currentDateReference?.child(foodKey)?.child(getString(R.string.amount).toLowerCase())?.setValue(amount)
        }

        val historyReference = mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid).child(getString(R.string.history).toLowerCase())
        }

        historyReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var foodInHistory: DatabaseReference? = null

                for (index in snapshot.children) {
                    if (index.child(getString(R.string.id).toLowerCase()).value.toString() == id) {
                        foodInHistory = index.ref
                    }
                }

                if (foodInHistory != null) {
                    foodInHistory.child(getString(R.string.amount).toLowerCase()).setValue(amount)
                } else {
                    historyReference.child(getString(R.string.history).toLowerCase() + "-$currentDate-$formattedTime").child(getString(R.string.id).toLowerCase())
                        .setValue(id)
                    historyReference.child(getString(R.string.history).toLowerCase() + "-$currentDate-$formattedTime").child(getString(R.string.amount).toLowerCase())
                        .setValue(amount)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun updateMacronutrients(amountEditable: Editable) {
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        val foodAmount = amountEditable.toString().toDouble()
        val carbohydratesAmount = (intent.getStringExtra(getString(R.string.carbohydratesAmount))
            ?.toInt()!! * foodAmount / 100)
        val fatsAmount =
            (intent.getStringExtra(getString(R.string.fatsAmount))?.toInt()!! * foodAmount / 100)
        val proteinsAmount =
            (intent.getStringExtra(getString(R.string.proteinsAmount))?.toInt()!! * foodAmount / 100)
        val caloriesAmount = intent.getStringExtra(getString(R.string.caloriesAmount)).toString().toInt()

        carbohydratesInfoAmount.text = carbohydratesAmount.toString() + getString(R.string.grams_short)
        fatsInfoAmount.text = fatsAmount.toString() + getString(R.string.grams_short)
        proteinsInfoAmount.text = proteinsAmount.toString() + getString(R.string.grams_short)

        val calories = (caloriesAmount * foodAmount) / 100
        val pieChart = findViewById<PieChart>(R.id.foodInfoPieChart)
        setPieChart(pieChart, calories.toInt())
    }

    private fun updateMacronutrientsToZero() {
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        carbohydratesInfoAmount.text = getString(R.string.zero_grams)
        fatsInfoAmount.text = getString(R.string.zero_grams)
        proteinsInfoAmount.text = getString(R.string.zero_grams)

        val pieChart = findViewById<PieChart>(R.id.foodInfoPieChart)
        setPieChart(pieChart, 0)
    }

    private fun setPieChart(
        pieChart: PieChart,
        calories: Int
    ) {
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 80f
        pieChart.centerText = calories.toString() + "\n" + getString(R.string.cal)
        pieChart.setCenterTextSize(25f)

        val values = ArrayList<PieEntry>()
        values.add(PieEntry(carbohydratesTotalPercent!!.toFloat()))
        values.add(PieEntry(fatsTotalPercent!!.toFloat()))
        values.add(PieEntry(proteinsTotalPercent!!.toFloat()))

        val pieDataSet = PieDataSet(values, getString(R.string.macronutrients))
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData

        val colors =
            arrayListOf(Color.rgb(66, 146, 227), Color.rgb(235, 73, 73), Color.rgb(99, 230, 129))
        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 0f

        pieChart.animateXY(1000, 1000)
    }

    fun foodDelete(item: MenuItem) {
        database = Firebase.database.reference
        val meal = intent.getStringExtra(getString(R.string.meal).toLowerCase()).toString()
        val foodKey = intent.getStringExtra(getString(R.string.key).toLowerCase()).toString()
        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()

        database.child(getString(R.string.users).toLowerCase()).child(mAuth.uid!!).child(getString(R.string.dates).toLowerCase()).child(currentDate).child(getString(R.string.diary).toLowerCase())
            .child(meal).child(foodKey).removeValue()

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}