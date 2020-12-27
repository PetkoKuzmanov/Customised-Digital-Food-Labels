package com.example.project.macronutrients

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
import kotlin.math.roundToInt

class MacronutrientsActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macronutrients)
        setSupportActionBar(findViewById(R.id.macronutrientsToolbar))

        database = Firebase.database.reference

        val carbohydratesAmount = findViewById<TextView>(R.id.carbohydratesAmount)
        val fatsAmount = findViewById<TextView>(R.id.fatsAmount)
        val proteinsAmount = findViewById<TextView>(R.id.proteinsAmount)

        val carbohydratesTotalTextView = findViewById<TextView>(R.id.carbohydratesTotal)
        val fatsTotalTextView = findViewById<TextView>(R.id.fatsTotal)
        val proteinsTotalTextView = findViewById<TextView>(R.id.proteinsTotal)

        val carbohydratesGoal = findViewById<TextView>(R.id.carbohydratesGoal)
        val fatsGoal = findViewById<TextView>(R.id.fatsGoal)
        val proteinsGoal = findViewById<TextView>(R.id.proteinsGoal)

        val caloriesTotalDouble = 1225.0
        val carbohydratesTotalInt = 175
        val fatsTotalInt = 25
        val proteinsTotalInt = 75

        val carbohydratesCalories = carbohydratesTotalInt.toInt() * 4
        val fatsCalories = fatsTotalInt.toInt() * 9
        val proteinsCalories = proteinsTotalInt.toInt() * 4

        carbohydratesAmount.text = carbohydratesTotalInt.toString() + "g"
        fatsAmount.text = fatsTotalInt.toString() + "g"
        proteinsAmount.text = proteinsTotalInt.toString() + "g"

        val carbohydratesTotalPercent =
            ((carbohydratesCalories / caloriesTotalDouble) * 100).roundToInt()
        val fatsTotalPercent = ((fatsCalories / caloriesTotalDouble) * 100).roundToInt()
        val proteinsTotalPercent = ((proteinsCalories / caloriesTotalDouble) * 100).roundToInt()

        carbohydratesTotalTextView.text = "$carbohydratesTotalPercent%"
        fatsTotalTextView.text = "$fatsTotalPercent%"
        proteinsTotalTextView.text = "$proteinsTotalPercent%"

        val databaseReference = mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("goals")
        }

        var carbohydrates = ""
        var fats = ""
        var proteins = ""

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    var calories = snapshot.child("calories").value.toString().toDouble()
                    carbohydrates = snapshot.child("carbohydrates").value.toString()
                    fats = snapshot.child("fats").value.toString()
                    proteins = snapshot.child("proteins").value.toString()

                    val carbohydratesGoalCalories = carbohydrates.toInt() * 4
                    val fatsGoalCalories = fats.toInt() * 9
                    val proteinsGoalCalories = proteins.toInt() * 4

                    if (calories == 0.0) {
                        calories = 1.0
                    }
                    val carbohydratesGoalPercent: Int =
                        ((carbohydratesGoalCalories / calories) * 100).roundToInt()
                    val fatsGoalPercent: Int = ((fatsGoalCalories / calories) * 100).roundToInt()
                    val proteinsGoalPercent: Int =
                        ((proteinsGoalCalories / calories) * 100).roundToInt()

                    carbohydratesGoal.text = "$carbohydratesGoalPercent%"
                    fatsGoal.text = "$fatsGoalPercent%"
                    proteinsGoal.text = "$proteinsGoalPercent%"
                } else {
                    carbohydratesGoal.text = "0%"
                    fatsGoal.text = "0%"
                    proteinsGoal.text = "0%"

                    carbohydrates = "0"
                    fats = "0"
                    proteins = "0"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val pieChart = findViewById<PieChart>(R.id.macronutrientsChart)
        setPieChart(pieChart, carbohydratesTotalPercent, fatsTotalPercent, proteinsTotalPercent)

        val goalsButton = findViewById<Button>(R.id.goalsButton)
        goalsButton.setOnClickListener {
            val intent = Intent(applicationContext, GoalsActivity::class.java)
            intent.putExtra("carbohydrates", carbohydrates)
            intent.putExtra("fats", fats)
            intent.putExtra("proteins", proteins)
            startActivity(intent)
        }
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

        var values = ArrayList<PieEntry>()
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

//    class MyValueFormatter(pieChart: PieChart) : PercentFormatter() {
//        private lateinit var pieChart: PieChart
//
//        fun MyValueFormatter() {
//            mFormat = DecimalFormat("##")
//        }
//
//    }
}