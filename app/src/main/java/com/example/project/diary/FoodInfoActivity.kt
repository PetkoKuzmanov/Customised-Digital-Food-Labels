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

        val foodAmount = intent.getStringExtra("amount").toString().toInt()
        val caloriesAmount = intent.getStringExtra("caloriesAmount").toString().toInt()

        foodNameTextView.text = intent.getStringExtra("name")
        foodDescriptionTextView.text = intent.getStringExtra("description")
        numberOfServingsEditText.text =
            Editable.Factory.getInstance().newEditable(foodAmount.toString())

        val carbohydratesAmount = intent.getStringExtra("carbohydratesAmount").toString().toInt()
        val fatsAmount = intent.getStringExtra("fatsAmount").toString().toInt()
        val proteinsAmount = intent.getStringExtra("proteinsAmount").toString().toInt()

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
        val menuString = intent.getStringExtra("menu")

        if (menuString == "addFood") {
            menuInflater.inflate((R.menu.food_info_toolbar_layout), menu)
        } else if (menuString == "diary") {
            menuInflater.inflate((R.menu.food_edit_info_toolbar_layout), menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    fun foodInfoNext(item: MenuItem) {
        val numberOfServingsEditText = findViewById<EditText>(R.id.quickAddEditText)
        database = Firebase.database.reference

        val foodKey = intent.getStringExtra("key")
        val meal = intent.getStringExtra("meal").toString()
        val id = intent.getStringExtra("id").toString()
        val amount = numberOfServingsEditText.text.toString()
        val currentDate = intent.getStringExtra("date").toString()

        val current = LocalDateTime.now()
        val formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss")
        val formattedTime = current.format(formatterTime)

        val currentDateReference = mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("dates").child(currentDate).child("diary")
                .child(meal)
        }

        if (foodKey == null) {
            currentDateReference?.child("food-$formattedTime")?.child("id")?.setValue(id)
            currentDateReference?.child("food-$formattedTime")?.child("amount")?.setValue(amount)
        } else {
            currentDateReference?.child(foodKey)?.child("amount")?.setValue(amount)
        }

        val historyReference = mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("history")
        }

        historyReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var foodInHistory: DatabaseReference? = null

                for (index in snapshot.children) {
                    if (index.child("id").value.toString() == id) {
                        foodInHistory = index.ref
                    }
                }

                if (foodInHistory != null) {
                    foodInHistory.child("amount").setValue(amount)
                } else {
                    historyReference.child("history-$currentDate-$formattedTime").child("id")
                        .setValue(id)
                    historyReference.child("history-$currentDate-$formattedTime").child("amount")
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
        val carbohydratesAmount = (intent.getStringExtra("carbohydratesAmount")
            ?.toInt()!! * foodAmount / 100)
        val fatsAmount =
            (intent.getStringExtra("fatsAmount")?.toInt()!! * foodAmount / 100)
        val proteinsAmount =
            (intent.getStringExtra("proteinsAmount")?.toInt()!! * foodAmount / 100)
        val caloriesAmount = intent.getStringExtra("caloriesAmount").toString().toInt()

        carbohydratesInfoAmount.text = carbohydratesAmount.toString() + "g"
        fatsInfoAmount.text = fatsAmount.toString() + "g"
        proteinsInfoAmount.text = proteinsAmount.toString() + "g"

        val calories = (caloriesAmount * foodAmount) / 100
        val pieChart = findViewById<PieChart>(R.id.foodInfoPieChart)
        setPieChart(pieChart, calories.toInt())
    }

    private fun updateMacronutrientsToZero() {
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        carbohydratesInfoAmount.text = "0.0g"
        fatsInfoAmount.text = "0.0g"
        proteinsInfoAmount.text = "0.0g"

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
        pieChart.centerText = "$calories\nCal"
        pieChart.setCenterTextSize(25f)

        val values = ArrayList<PieEntry>()
        values.add(PieEntry(carbohydratesTotalPercent!!.toFloat()))
        values.add(PieEntry(fatsTotalPercent!!.toFloat()))
        values.add(PieEntry(proteinsTotalPercent!!.toFloat()))

        val pieDataSet = PieDataSet(values, "Macronutrients")
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
        val meal = intent.getStringExtra("meal").toString()
        val foodKey = intent.getStringExtra("key").toString()
        val currentDate = intent.getStringExtra("date").toString()

        database.child("users").child(mAuth.uid!!).child("dates").child(currentDate).child("diary")
            .child(meal).child(foodKey).removeValue()

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}