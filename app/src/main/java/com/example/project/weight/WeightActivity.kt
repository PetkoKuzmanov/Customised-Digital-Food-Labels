package com.example.project.weight

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*


class WeightActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)
        setSupportActionBar(findViewById(R.id.weightToolbar))

        getWeight()
    }

    private fun getWeight() {
        val weightModelList = ArrayList<WeightModel>()
        val mutableMap: MutableMap<String, Double> = mutableMapOf()

        database = Firebase.database.reference
        val databaseReference = mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                .child(getString(R.string.dates).toLowerCase())
        }

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                weightModelList.clear()
                mutableMap.clear()

                for (index in snapshot.children) {
                    val dateSnapshot = index.child(getString(R.string.weight).toLowerCase())

                    if (dateSnapshot.exists()) {
                        val date = index.key.toString()
                        val weight = dateSnapshot.value.toString().toDouble()
                        mutableMap[date] = weight
                    }
                }

                if (mutableMap.isNotEmpty()) {
                    for (index in mutableMap.entries.reversed()) {
                        val weightModel = WeightModel()
                        weightModel.setDate(index.key)
                        weightModel.setWeight(index.value)
                        weightModelList.add(weightModel)
                    }

                    val lineChart = findViewById<LineChart>(R.id.weightLineChart)
                    setLineChart(lineChart, mutableMap)
                }


                val recyclerView = findViewById<RecyclerView>(R.id.weightRecyclerView)
                val layoutManager = LinearLayoutManager(context)
                recyclerView.layoutManager = layoutManager

                val mAdapter = WeightAdapter(weightModelList)
                recyclerView.adapter = mAdapter
                findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
                    addWeightDialog(mAdapter)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun addWeightDialog(mAdapter: WeightAdapter) {
        val inflater = LayoutInflater.from(this)
        val addWeightView = inflater.inflate(R.layout.add_weight_layout, null)
        val enterWeight = addWeightView.findViewById(R.id.enterWeight) as EditText
        val dateTextView = addWeightView.findViewById<TextView>(R.id.datePickerTextView)

        //Set the current date as the date
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(getString(R.string.date_format))
        val formattedDate = current.format(formatter)
        dateTextView.text = formattedDate

        //Add a datePicker
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerButton = addWeightView.findViewById<Button>(R.id.datePickerButton)
        datePickerButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    if (dayOfMonth < 10) {
                        if (month + 1 < 10) {
                            dateTextView.text =
                                ("$year-" + getString(R.string.zero) + (month + 1) + "-0$dayOfMonth")
                        } else {
                            dateTextView.text = ("$year-" + (month + 1) + "-0$dayOfMonth")
                        }
                    } else {
                        if (month + 1 < 10) {
                            dateTextView.text =
                                ("$year-" + getString(R.string.zero) + (month + 1) + "-$dayOfMonth")
                        } else {
                            dateTextView.text = ("$year-" + (month + 1) + "-$dayOfMonth")
                        }
                    }
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        //Create the alertDialog
        val builder = AlertDialog.Builder(this)
            .setView(addWeightView)
        builder.create()
        builder.setPositiveButton(getString(R.string.add).toUpperCase()) { _, _ ->
            val input = enterWeight.text.toString()
            val fab = findViewById<FloatingActionButton>(R.id.fab)
            var weight = 0.0

            val snackbar = Snackbar.make(
                fab, getString(R.string.please_input_a_number),
                Snackbar.LENGTH_LONG
            )
            snackbar.anchorView = fab

            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
                weight = input.toDouble()
                mAdapter.addItem(weight, dateTextView.text.toString())
            } else {
                snackbar.show()
            }
        }
        builder.setNegativeButton(getString(R.string.cancel).toUpperCase()) { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    private fun setLineChart(lineChart: LineChart, mutableMap: MutableMap<String, Double>) {
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

        lineChart.animateX(0, Easing.EaseInExpo)

        val xAxis = lineChart.xAxis
        val leftAxis = lineChart.axisLeft

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisRight.isEnabled = false

        val dateList = arrayListOf<String>()
        val weightList = arrayListOf<Double>()

        for (index in mutableMap) {
            dateList.add(index.key)
            weightList.add(index.value)
        }

        var weightMin = weightList[0]
        var weightMax = weightList[0]

        for (index in weightList) {
            if (index < weightMin) {
                weightMin = index
            } else if (index > weightMax) {
                weightMax = index
            }
        }

        xAxis.valueFormatter = ClaimsXAxisValueFormatter(dateList)
        leftAxis.valueFormatter = ClaimsYAxisValueFormatter()

        xAxis.setDrawGridLines(false)
//        xAxis.axisMaximum = dateList.size.toFloat() - 1
        xAxis.granularity = 1f

        leftAxis.granularity = 1f
        leftAxis.axisMinimum = (weightMin - 0.5).toFloat()
        leftAxis.axisMaximum = (weightMax + 0.5).toFloat()
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawZeroLine(false)
        leftAxis.setDrawLimitLinesBehindData(false)

        val values = ArrayList<Entry>()
        for (i in 0 until weightList.size) {
            values.add(Entry(i.toFloat(), weightList[i].toFloat()))
        }

        val lineDataSet = LineDataSet(values, getString(R.string.weight))
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = R.color.design_default_color_primary
        lineDataSet.setDrawValues(false)
    }

    class ClaimsXAxisValueFormatter(var datesList: List<String>) :
        ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            var position = value.roundToInt()
            val sdf = SimpleDateFormat("MMM dd")
            if (value < 1) {
                position = 0
            }
//            } else if (value > 1 && value < 2) {
//                position = 0
//            } else if (value > 2 && value < 3) {
//                position = 1
//            } else if (value > 3 && value < 4) {
//                position = 2
//            } else if (value > 4 && value <= 5) {
//                position = 3
//            }
            if (position < datesList.size) {
                return sdf.format(
                    Date(
                        (getDateInMilliSeconds(
                            datesList[position],
                            "yyyy-MM-dd",
                        ))
                    )
                )
            }
            return ""
        }

        private fun getDateInMilliSeconds(givenDateString: String?, format: String): Long {
            val sdf = SimpleDateFormat(format, Locale.US)
            var timeInMilliseconds: Long = 1
            try {
                val mDate = sdf.parse(givenDateString)
                timeInMilliseconds = mDate.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return timeInMilliseconds
        }
    }

    class ClaimsYAxisValueFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            return value.roundToInt().toString()
        }
    }
}