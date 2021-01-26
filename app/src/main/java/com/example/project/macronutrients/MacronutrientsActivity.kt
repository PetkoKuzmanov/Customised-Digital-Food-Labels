package com.example.project.macronutrients

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

class MacronutrientsActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macronutrients)
        setSupportActionBar(findViewById(R.id.macronutrientsToolBar))
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
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

        var carbohydrates = ""
        var fats = ""
        var proteins = ""

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val goalsSnapshot = snapshot.child("users").child(mAuth.uid!!).child("goals")
                if (goalsSnapshot.hasChildren()) {
                    var calories = goalsSnapshot.child("calories").value.toString().toDouble()
                    carbohydrates = goalsSnapshot.child("carbohydrates").value.toString()
                    fats = goalsSnapshot.child("fats").value.toString()
                    proteins = goalsSnapshot.child("proteins").value.toString()

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

                //Get the total for today
                val currentDate = LocalDateTime.now()
                val formatterDate: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = currentDate.format(formatterDate)
                val todaySnapshot =
                    snapshot.child("users").child(mAuth.uid!!).child("dates").child(formattedDate)
                        .child("diary")

                var caloriesTotalDouble = 0.0
                var carbohydratesTotalInt = 0
                var fatsTotalInt = 0
                var proteinsTotalInt = 0

                if (todaySnapshot.hasChildren()) {
                    for (meal in todaySnapshot.children) {
                        if (meal.key != "exercise") {
                            for (food in meal.children) {
                                if (food.key.toString().contains("food")) {

                                    val id = food.child("id").value.toString()
                                    val amount = food.child("amount").value.toString().toInt()

                                    val foodSnapshot = snapshot.child("food").child(id)

                                    val caloriesPerHundred =
                                        foodSnapshot.child("calories").value.toString().toInt()
                                    val carbohydratesPerHundred =
                                        foodSnapshot.child("carbohydrates").value.toString().toInt()
                                    val fatsPerHundred =
                                        foodSnapshot.child("fats").value.toString().toInt()
                                    val proteinsPerHundred =
                                        foodSnapshot.child("proteins").value.toString().toInt()

                                    val calories = (caloriesPerHundred * amount) / 100
                                    val foodCarbohydrates = (carbohydratesPerHundred * amount) / 100
                                    val foodFats = (fatsPerHundred * amount) / 100
                                    val foodProteins = (proteinsPerHundred * amount) / 100

                                    caloriesTotalDouble += foodCarbohydrates * 4 + foodFats * 9 + foodProteins * 4
                                    carbohydratesTotalInt += foodCarbohydrates
                                    fatsTotalInt += foodFats
                                    proteinsTotalInt += foodProteins
                                }
                            }
                        }
                    }
                } else {
                    caloriesTotalDouble = 1.0
                }
                val carbohydratesCalories = carbohydratesTotalInt * 4
                val fatsCalories = fatsTotalInt * 9
                val proteinsCalories = proteinsTotalInt * 4

                carbohydratesAmount.text = carbohydratesTotalInt.toString() + "g"
                fatsAmount.text = fatsTotalInt.toString() + "g"
                proteinsAmount.text = proteinsTotalInt.toString() + "g"

                val carbohydratesTotalPercent =
                    ((carbohydratesCalories / caloriesTotalDouble) * 100).roundToInt()
                val fatsTotalPercent = ((fatsCalories / caloriesTotalDouble) * 100).roundToInt()
                val proteinsTotalPercent =
                    ((proteinsCalories / caloriesTotalDouble) * 100).roundToInt()

                carbohydratesTotalTextView.text = "$carbohydratesTotalPercent%"
                fatsTotalTextView.text = "$fatsTotalPercent%"
                proteinsTotalTextView.text = "$proteinsTotalPercent%"

                val pieChart = findViewById<PieChart>(R.id.macronutrientsChart)
                setPieChart(
                    pieChart,
                    carbohydratesTotalPercent,
                    fatsTotalPercent,
                    proteinsTotalPercent
                )
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

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

}