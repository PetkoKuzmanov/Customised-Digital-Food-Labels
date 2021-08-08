package com.petko.project.macronutrients

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.petko.project.R
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
                val goalsSnapshot =
                    snapshot.child(getString(R.string.users).toLowerCase()).child(mAuth.uid!!)
                        .child(getString(R.string.goals).toLowerCase())
                if (goalsSnapshot.hasChildren()) {
                    var calories =
                        goalsSnapshot.child(getString(R.string.calories).toLowerCase()).value.toString()
                            .toDouble()
                    carbohydrates =
                        goalsSnapshot.child(getString(R.string.carbohydrates).toLowerCase()).value.toString()
                    fats =
                        goalsSnapshot.child(getString(R.string.fats).toLowerCase()).value.toString()
                    proteins =
                        goalsSnapshot.child(getString(R.string.proteins).toLowerCase()).value.toString()

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

                    carbohydratesGoal.text =
                        carbohydratesGoalPercent.toString() + getString(R.string.percent_sign)
                    fatsGoal.text = fatsGoalPercent.toString() + getString(R.string.percent_sign)
                    proteinsGoal.text =
                        proteinsGoalPercent.toString() + getString(R.string.percent_sign)

                } else {
                    carbohydratesGoal.text = getString(R.string.zero_percent)
                    fatsGoal.text = getString(R.string.zero_percent)
                    proteinsGoal.text = getString(R.string.zero_percent)

                    carbohydrates = getString(R.string.zero)
                    fats = getString(R.string.zero)
                    proteins = getString(R.string.zero)
                }

                //Get the total for today
                val currentDate = LocalDateTime.now()
                val formatterDate: DateTimeFormatter =
                    DateTimeFormatter.ofPattern(getString(R.string.date_format))
                val formattedDate = currentDate.format(formatterDate)
                val todaySnapshot =
                    snapshot.child(getString(R.string.users).toLowerCase()).child(mAuth.uid!!)
                        .child(getString(R.string.dates).toLowerCase()).child(formattedDate)
                        .child(getString(R.string.diary).toLowerCase())

                var caloriesTotalDouble = 0.0
                var carbohydratesTotalInt = 0
                var fatsTotalInt = 0
                var proteinsTotalInt = 0

                if (todaySnapshot.hasChildren()) {
                    for (meal in todaySnapshot.children) {
                        if (meal.key != getString(R.string.exercise).toLowerCase()) {
                            for (food in meal.children) {
                                if (food.key.toString()
                                        .contains(getString(R.string.food).toLowerCase())
                                ) {

                                    val id =
                                        food.child(getString(R.string.id).toLowerCase()).value.toString()
                                    val amount =
                                        food.child(getString(R.string.amount).toLowerCase()).value.toString()
                                            .toInt()

                                    val foodSnapshot =
                                        snapshot.child(getString(R.string.food).toLowerCase())
                                            .child(id)

                                    val carbohydratesPerHundred =
                                        foodSnapshot.child(getString(R.string.carbohydrates).toLowerCase()).value.toString()
                                            .toInt()
                                    val fatsPerHundred =
                                        foodSnapshot.child(getString(R.string.fats).toLowerCase()).value.toString()
                                            .toInt()
                                    val proteinsPerHundred =
                                        foodSnapshot.child(getString(R.string.proteins).toLowerCase()).value.toString()
                                            .toInt()

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

                carbohydratesAmount.text =
                    carbohydratesTotalInt.toString() + getString(R.string.grams_short)
                fatsAmount.text = fatsTotalInt.toString() + getString(R.string.grams_short)
                proteinsAmount.text = proteinsTotalInt.toString() + getString(R.string.grams_short)

                val carbohydratesTotalPercent =
                    ((carbohydratesCalories / caloriesTotalDouble) * 100).roundToInt()
                val fatsTotalPercent = ((fatsCalories / caloriesTotalDouble) * 100).roundToInt()
                val proteinsTotalPercent =
                    ((proteinsCalories / caloriesTotalDouble) * 100).roundToInt()

                carbohydratesTotalTextView.text =
                    carbohydratesTotalPercent.toString() + getString(R.string.percent_sign)
                fatsTotalTextView.text =
                    fatsTotalPercent.toString() + getString(R.string.percent_sign)
                proteinsTotalTextView.text =
                    proteinsTotalPercent.toString() + getString(R.string.percent_sign)

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
            intent.putExtra(getString(R.string.carbohydrates).toLowerCase(), carbohydrates)
            intent.putExtra(getString(R.string.fats).toLowerCase(), fats)
            intent.putExtra(getString(R.string.proteins).toLowerCase(), proteins)
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

        val pieDataSet = PieDataSet(values, getString(R.string.macronutrients))
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData

        val colors =
            arrayListOf(Color.rgb(66, 146, 227), Color.rgb(235, 73, 73), Color.rgb(99, 230, 129))
        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 20f
        pieDataSet.valueFormatter = PercentFormatter(pieChart)

        pieChart.animateXY(0, 0)
    }

}