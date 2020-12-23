package com.example.project.macronutrients

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat
import kotlin.math.roundToInt

class MacronutrientsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_macronutrients)
        setSupportActionBar(findViewById(R.id.macronutrientsToolbar))

        val pieChart = findViewById<PieChart>(R.id.macronutrientsChart)
        setPieChart(pieChart)
    }

    private fun setPieChart(pieChart: PieChart) {
        pieChart.setUsePercentValues(true)

        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = false
        pieChart.legend.isEnabled = false

        var values = ArrayList<PieEntry>()
        values.add(PieEntry(45f))
        values.add(PieEntry(30f))
        values.add(PieEntry(25f))

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