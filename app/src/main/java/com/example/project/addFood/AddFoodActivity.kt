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
import android.widget.Filterable
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.diary.DiaryDayAdapter
import com.example.project.diary.FoodModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_add_food.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class AddFoodActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val textRequestCode = 100
    private val barcodeRequestCode = 200
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(findViewById(R.id.addFoodToolbar))

        populateList()

        val searchView = findViewById<SearchView>(R.id.foodSearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                println("TEXT SUBMIT")
                (historyRecyclerView.adapter as Filterable).filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (historyRecyclerView.adapter as Filterable).filter.filter(newText)
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.add_food_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun populateList() {
        val historyFoodModelList = ArrayList<FoodModel>()

        val historyNamesList = arrayListOf<String>(
        )
        val historyDescriptionsList = arrayListOf<String>(
        )
        val historyAmountsList = arrayListOf<String>(
        )
        val historyAmountMeasurementsList = arrayListOf<String>(
        )
        val historyCaloriesAmountsList = arrayListOf<String>(
        )

        database = Firebase.database.reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyFoodModelList.clear()
                historyNamesList.clear()
                historyDescriptionsList.clear()
                historyAmountsList.clear()
                historyAmountMeasurementsList.clear()
                historyCaloriesAmountsList.clear()

                val foodHistoryReference = mAuth.currentUser?.let {
                    snapshot.child("users").child(it.uid).child("history")
                }

                for (index in foodHistoryReference?.children!!) {

                    val foodId = index.child("id").value.toString()
                    val foodAmount = index.child("amount").value.toString()

                    val foodReference = snapshot.child("food").child(foodId)

                    val foodName = foodReference.child("name").value.toString()
                    val foodDescription = foodReference.child("description").value.toString()
                    val foodAmountMeasurement = foodReference.child("measurement").value.toString()
                    val foodCalories = foodReference.child("calories").value.toString().toInt()

                    val foodInstanceCalories =
                        ((foodCalories * foodAmount.toDouble()) / 100).roundToInt().toString()

                    historyNamesList.add(foodName)
                    historyDescriptionsList.add(foodDescription)
                    historyAmountsList.add(foodAmount)
                    historyAmountMeasurementsList.add(foodAmountMeasurement)
                    historyCaloriesAmountsList.add(foodInstanceCalories)
                }

                for (i: Int in 0 until historyNamesList.size) {
                    val foodModel = FoodModel()
                    foodModel.setName(historyNamesList[i])
                    foodModel.setWeight(historyDescriptionsList[i])
                    foodModel.setAmount(historyAmountsList[i])
                    foodModel.setMeasurement(historyAmountMeasurementsList[i])
                    foodModel.setCaloriesAmount(historyCaloriesAmountsList[i])
                    historyFoodModelList.add(foodModel)
                }

                val layoutManager = LinearLayoutManager(context)
                val historyRecyclerView = historyRecyclerView
                historyRecyclerView.layoutManager = layoutManager
                val historyAdapter = AddFoodAdapter(historyFoodModelList)
                historyRecyclerView.adapter = historyAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
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