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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import kotlin.math.roundToInt


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
            database.child("users").child(it.uid).child("dates")
        }

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                weightModelList.clear()
                mutableMap.clear()

                for (index in snapshot.children) {
                    val dateSnapshot = index.child("weight")

                    val date = index.key.toString()
                    val weight = dateSnapshot.value.toString().toDouble()
                    mutableMap[date] = weight

                }

                if (mutableMap.isNotEmpty()) {
                    for (index in mutableMap.entries.reversed()) {
                        val weightModel = WeightModel()
                        weightModel.setDate(index.key)
                        weightModel.setWeight(index.value)
                        weightModelList.add(weightModel)
                    }
                }

                val lineChart = findViewById<LineChart>(R.id.weightLineChart)
                setLineChart(lineChart)

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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
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
                        dateTextView.text = ("$year-" + (month + 1) + "-0$dayOfMonth")
                    } else {
                        dateTextView.text = ("$year-" + (month + 1) + "-$dayOfMonth")
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
        builder.setPositiveButton("ADD") { _, _ ->
            val input = enterWeight.text.toString()
            val fab = findViewById<FloatingActionButton>(R.id.fab)
            var weight = 0.0

//            val snackbar = Snackbar.make(
//                this, "You have to input a number",
//                Snackbar.LENGTH_LONG
//            )
//            snackbar.anchorView = fab

            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
                weight = input.toDouble()
                mAdapter.addItem(weight, dateTextView.text.toString())
            } else {
//                snackbar.show()
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    private fun setLineChart(lineChart: LineChart) {
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false

//        val limitLineOne = LimitLine(30f, "Title")
//
//        limitLineOne.lineColor = Color.rgb(66, 146, 227)
//        limitLineOne.lineWidth = 4f
//        limitLineOne.enableDashedLine(10f, 10f, 0f)
//        limitLineOne.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
//        limitLineOne.textSize = 10f
//
//        val limitLineTwo = LimitLine(35f, "")
//        limitLineTwo.lineWidth = 4f
//        limitLineOne.enableDashedLine(10f, 10f, 0f)

        val xAxis = lineChart.xAxis
        val leftAxis = lineChart.axisLeft

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisRight.isEnabled = false


        val dates = arrayListOf(
            "2020-10-29",
            "2020-10-30",
            "2020-10-31",
            "2020-11-01",
        )

        xAxis.valueFormatter = ClaimsXAxisValueFormatter(dates)
        leftAxis.valueFormatter = ClaimsYAxisValueFormatter()

        xAxis.setDrawGridLines(false)
//        xAxis.axisMaximum = 15f
//        xAxis.axisMinimum = 0f

        leftAxis.axisMinimum = 62f
        leftAxis.axisMaximum = 68f
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawZeroLine(false)
        leftAxis.setDrawLimitLinesBehindData(false)

        var values = ArrayList<Entry>()

        values.add(Entry(1f, 64.3f))
        values.add(Entry(2f, 63.5f))
        values.add(Entry(3f, 63.7f))
        values.add(Entry(4f, 63.9f))


        val lineDataSet = LineDataSet(values, "Weight")
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = R.color.design_default_color_primary
        lineDataSet.setDrawValues(false)
    }

    class ClaimsXAxisValueFormatter(var datesList: List<String>) :
        ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase): String {

            var position = Math.round(value)
            val sdf = SimpleDateFormat("MMM dd")
            if (value > 1 && value < 2) {
                position = 0
            } else if (value > 2 && value < 3) {
                position = 1
            } else if (value > 3 && value < 4) {
                position = 2
            } else if (value > 4 && value <= 5) {
                position = 3
            }
            if (position < datesList.size)
                return sdf.format(
                    Date(
                        (getDateInMilliSeconds(
                            datesList.get(position),
                            "yyyy-MM-dd"
                        ))
                    )
                );
            return "";
        }

        fun getDateInMilliSeconds(givenDateString: String?, format: String): Long {
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