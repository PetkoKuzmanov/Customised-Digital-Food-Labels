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
    val datesList: ArrayList<String> = arrayListOf(

    )
    val weightsList: ArrayList<Double> = arrayListOf(

    )
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)
        setSupportActionBar(findViewById(R.id.weightToolbar))

        val weightModel = WeightModel()
        weightModel.setDate("22-12-2020")
        weightModel.setWeight(63.0)
        val mutableWeight = MutableList(1) {
            weightModel

        }
        val mAdapter = WeightAdapter(mutableWeight)

        val weightView = findViewById<RecyclerView>(R.id.weightRecyclerView)
        val linearLayoutManager = LinearLayoutManager(this)
        weightView.layoutManager = linearLayoutManager
        weightView.adapter = mAdapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            addWeightDialog(mAdapter)
        }

        val lineChart = findViewById<LineChart>(R.id.weightLineChart)
        setLineChart(lineChart)
        getWeight()
    }

    private fun addWeightDialog(mAdapter: WeightAdapter) {
        val inflater = LayoutInflater.from(this)
        val addWeightView = inflater.inflate(R.layout.add_weight_layout, null)
        val enterWeight = addWeightView.findViewById(R.id.enterWeight) as EditText
        val dateTextView = addWeightView.findViewById<TextView>(R.id.datePickerTextView)

        //Set the current date as the date
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)
        dateTextView.text = formatted

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
                    dateTextView.text = ("$dayOfMonth-" + (month + 1) + "-$year")
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

    private fun getWeight() {
        val weightModelList = ArrayList<WeightModel>()

        database = Firebase.database.reference
        val databaseReference = mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("weight")
        }

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                datesList.clear()
                weightsList.clear()
                weightModelList.clear()

                for (dateSnapshot in snapshot.children) {
                    val date = dateSnapshot.key.toString()
                    val weight = dateSnapshot.value.toString().toDouble()
                    datesList.add(date)
                    weightsList.add(weight)
                }

                if (datesList.isNotEmpty() && weightsList.isNotEmpty()) {
                    for (i: Int in 0 until datesList.size) {
                        val weightModel = WeightModel()
                        weightModel.setDate(datesList[i])
                        weightModel.setWeight(weightsList[i])
                        weightModelList.add(weightModel)
                    }

                    val recyclerView = findViewById<RecyclerView>(R.id.weightRecyclerView)
                    val layoutManager = LinearLayoutManager(context)
                    recyclerView.layoutManager = layoutManager
                    val mAdapter = WeightAdapter(weightModelList)
                    recyclerView.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

//        val datesList = arrayListOf(
//            "12 November 2020",
//            "11 November 2020",
//            "10 November 2020",
//            "9 November 2020",
//            "8 November 2020",
//            "7 November 2020",
//            "6 November 2020",
//            "5 November 2020",
//            "4 November 2020",
//            "3 November 2020",
//            "2 November 2020",
//            "1 November 2020",
//            "31 October 2020",
//            "30 October 2020",
//            "29 October 2020"
//        )
//        val weightAmountList = arrayListOf(
//            65.0, 65.1, 64.9, 64.9, 64.7, 64.8, 64.6, 64.3, 64.2, 64.2, 64.5, 63.9, 63.7, 63.5, 64.3
//        )
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
            "2020-11-02",
            "2020-11-03",
            "2020-11-04",
            "2020-11-05",
            "2020-11-06",
            "2020-11-07",
            "2020-11-08",
            "2020-11-09",
            "2020-11-10",
            "2020-11-11",
            "2020-11-12"
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
        values.add(Entry(5f, 64.5f))
        values.add(Entry(6f, 64.2f))
        values.add(Entry(7f, 64.2f))
        values.add(Entry(8f, 64.3f))
        values.add(Entry(9f, 64.6f))
        values.add(Entry(10f, 64.8f))
        values.add(Entry(11f, 64.7f))
        values.add(Entry(12f, 64.9f))
        values.add(Entry(13f, 64.9f))
        values.add(Entry(14f, 65.1f))
        values.add(Entry(15f, 65f))

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