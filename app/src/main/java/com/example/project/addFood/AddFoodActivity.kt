package com.example.project.addFood

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.diary.DiaryDayAdapter
import com.example.project.diary.FoodModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddFoodActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val textRequestCode = 100
    private val barcodeRequestCode = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(findViewById(R.id.addFoodToolbar))

        populateList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.add_food_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun populateList() {
        val historyFoodList = ArrayList<FoodModel>()

        val breakfastNamesList = arrayListOf(
            "Oats", "Milk", "Orange juice"
        )
        val breakfastDescriptionsList = arrayListOf(
            "Quaker", "Tesco", "Juicerino"
        )
        val breakfastAmountsList = arrayListOf(
            "50", "200", "250"
        )
        val breakfastAmountMeasurementsList = arrayListOf(
            "g", "ml", "ml"
        )
        val breakfastCaloriesAmountsList = arrayListOf(
            "150", "150", "125"
        )

        for (i: Int in 0 until 3) {
            val foodModel = FoodModel()
            foodModel.setDate(breakfastNamesList[i])
            foodModel.setWeight(breakfastDescriptionsList[i])
            foodModel.setAmount(breakfastAmountsList[i])
            foodModel.setMeasurement(breakfastAmountMeasurementsList[i])
            foodModel.setCaloriesAmount(breakfastCaloriesAmountsList[i])
            historyFoodList.add(foodModel)
        }

        val layoutManager = LinearLayoutManager(this)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = layoutManager
        val historyAdapter = DiaryDayAdapter(historyFoodList)
        historyRecyclerView.adapter = historyAdapter
    }

    fun quickAdd(item: MenuItem) {
        val inflater = LayoutInflater.from(this)
        val quickAddView = inflater.inflate(R.layout.quick_add_layout, null)
        val enterCalories = quickAddView.findViewById(R.id.enterCalories) as EditText
        val section = intent.getStringExtra("section").toString()

        database = Firebase.database.reference

        //Set the current date as the date
        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss")

        val formattedDate = current.format(formatterDate)
        val formattedTime = current.format(formatterTime)

        //Create the alertDialog
        val builder = AlertDialog.Builder(this)
            .setView(quickAddView)
            .setTitle("Quick add")
        builder.create()
        builder.setPositiveButton("ADD") { _, _ ->
            val input = enterCalories.text.toString()


            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
//                mAdapter.addItem(weight, dateTextView.text.toString())
//                mAuth.currentUser?.let {
//                    database.child("users").child(it.uid).child(formattedDate).child("diary")
//                        .child(section).child(formattedTime).child("id").setValue(id)
//                }
                val calories = input.toInt()
                mAuth.currentUser?.let {
                    database.child("users").child(it.uid).child("dates").child(formattedDate)
                        .child("diary")
                        .child(section).child("calories-$formattedTime").setValue(calories)
                }
                this.onBackPressed()
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    fun scanItem(item: MenuItem) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, barcodeRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == textRequestCode) {
            when (resultCode) {
                RESULT_OK -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    textRecognition(photo)
                }
                RESULT_CANCELED -> {
                    println("Operation cancelled by user")
                }
                else -> {
                    println("Failed to capture image")
                }
            }
        }

        if (requestCode == barcodeRequestCode) {
            if (resultCode == RESULT_OK) {
                val photo = data?.extras?.get("data") as Bitmap
                barcodeRecognition(photo)
            }
        }
    }

    private fun barcodeRecognition(photo: Bitmap) {
        val image = InputImage.fromBitmap(photo, 0)
        val scanner = BarcodeScanning.getClient()

        val result = scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                if (barcodes.size == 0) {
                    println("WWWWWWWWWWWWWW")
                    addBarcodeManuallyAlert()
                } else {
                    for (barcode in barcodes) {
                        val bounds = barcode.boundingBox
                        val corners = barcode.cornerPoints

                        val rawValue = barcode.rawValue
                        println("AAAAAAAAAAAAAAAAAAAAAAAAAA" + rawValue)

                    }
                }
            }
            .addOnFailureListener {
                // Task failed with an exception
                println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCC")
            }
    }

    private fun textRecognition(photo: Bitmap) {

    }

    private fun addBarcodeManuallyAlert() {
        val inflater = LayoutInflater.from(this)
        val addBarcodeView = inflater.inflate(R.layout.add_barcode_manually_layout, null)
        val enterBarcode = addBarcodeView.findViewById(R.id.enterBarcode) as EditText

        //Create the alertDialog
        val builder = AlertDialog.Builder(this)
            .setView(addBarcodeView)
            .setTitle("Could not find barcode")
        builder.create()
        builder.setPositiveButton("ADD") { _, _ ->
            val input = enterBarcode.text.toString()

            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
                val intent = Intent(applicationContext, AddFoodInfoActivity::class.java)
                intent.putExtra("barcode", input)
                startActivity(intent)
            } else {
                //input is empty
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }
}