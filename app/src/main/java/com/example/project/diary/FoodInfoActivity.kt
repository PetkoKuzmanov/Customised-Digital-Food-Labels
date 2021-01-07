package com.example.project.diary

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.project.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
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

        updateMacronutrients(numberOfServingsEditText.text)

        val carbohydratesAmount = intent.getStringExtra("carbohydratesAmount").toString().toInt()
        val fatsAmount = intent.getStringExtra("fatsAmount").toString().toInt()
        val proteinsAmount = intent.getStringExtra("proteinsAmount").toString().toInt()

        val carbohydratesTotalPercent =
            (((carbohydratesAmount * 4.0) / caloriesAmount) * 100).roundToInt()
        val fatsTotalPercent = (((fatsAmount * 9.0) / caloriesAmount) * 100).roundToInt()
        val proteinsTotalPercent = (((proteinsAmount * 4.0) / caloriesAmount) * 100).roundToInt()

        val pieChart = findViewById<PieChart>(R.id.foodInfoPieChart)
        setPieChart(pieChart, carbohydratesTotalPercent, fatsTotalPercent, proteinsTotalPercent)

        numberOfServingsEditText.doAfterTextChanged { editable ->
            if (editable?.isNotEmpty()!!) {
                updateMacronutrients(editable)
            } else {
                updateMacronutrientsToZero()
            }
        }
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

        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss")

        val formattedDate = current.format(formatterDate)
        val formattedTime = current.format(formatterTime)

        val databaseReference = mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("dates").child(formattedDate).child("diary")
                .child(meal)
        }

        if (foodKey == null) {
            databaseReference?.child("food-$formattedTime")?.child("id")?.setValue(id)
            databaseReference?.child("food-$formattedTime")?.child("amount")?.setValue(amount)
        } else {
            databaseReference?.child(foodKey)?.child("amount")?.setValue(amount)
        }

        this.onBackPressed()
    }

    private fun updateMacronutrients(amountEditable: Editable) {
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        val foodAmount = amountEditable.toString().toDouble()
        val carbohydratesAmount = (intent.getStringExtra("carbohydratesAmount")
            ?.toInt()!! * foodAmount / 100).toString()
        val fatsAmount =
            (intent.getStringExtra("fatsAmount")?.toInt()!! * foodAmount / 100)
                .toString()
        val proteinsAmount =
            (intent.getStringExtra("proteinsAmount")?.toInt()!! * foodAmount / 100)
                .toString()

        carbohydratesInfoAmount.text = carbohydratesAmount + "g"
        fatsInfoAmount.text = fatsAmount + "g"
        proteinsInfoAmount.text = proteinsAmount + "g"
    }

    private fun updateMacronutrientsToZero() {
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        carbohydratesInfoAmount.text = "0g"
        fatsInfoAmount.text = "0g"
        proteinsInfoAmount.text = "0g"
    }

    private fun setPieChart(
        pieChart: PieChart,
        carbohydratesTotalPercent: Int,
        fatsTotalPercent: Int,
        proteinsTotalPercent: Int
    ) {
        pieChart.setUsePercentValues(true)

        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = false
        pieChart.legend.isEnabled = false

        val values = ArrayList<PieEntry>()
        values.add(PieEntry(carbohydratesTotalPercent.toFloat()))
        values.add(PieEntry(fatsTotalPercent.toFloat()))
        values.add(PieEntry(proteinsTotalPercent.toFloat()))

        val pieDataSet = PieDataSet(values, "Macronutrients")
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData

        val colors =
            arrayListOf(Color.rgb(66, 146, 227), Color.rgb(235, 73, 73), Color.rgb(99, 230, 129))
        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 20f
        pieDataSet.valueFormatter = PercentFormatter(pieChart)

        pieChart.animateXY(1000, 1000)
    }

    fun foodDelete(item: MenuItem) {
        database = Firebase.database.reference
        val meal = intent.getStringExtra("meal").toString()
        val foodKey = intent.getStringExtra("key").toString()

        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = current.format(formatterDate)

        mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("dates").child(formattedDate).child("diary")
                .child(meal).child(foodKey).removeValue()
        }
        this.onBackPressed()
    }
}