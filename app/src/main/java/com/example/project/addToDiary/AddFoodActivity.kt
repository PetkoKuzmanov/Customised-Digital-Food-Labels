package com.example.project.addToDiary

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
import com.example.project.R
import com.example.project.diary.FoodInfoActivity
import com.example.project.diary.FoodModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_add_food.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddFoodActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val addFoodRequestCode = 123
    val context = this

    private var historyFoodModelList = ArrayList<FoodModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(findViewById(R.id.addFoodToolbar))

        populateList()

        val searchView = findViewById<SearchView>(R.id.foodSearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
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
        val currentDate = intent.getStringExtra("date").toString()

        database = Firebase.database.reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyFoodModelList.clear()

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
                    val foodCarbohydrates = foodReference.child("carbohydrates").value.toString()
                    val foodFats = foodReference.child("fats").value.toString()
                    val foodProteins = foodReference.child("proteins").value.toString()

                    val foodModel = FoodModel()
                    foodModel.setName(foodName)
                    foodModel.setId(foodId)
                    foodModel.setDescription(foodDescription)
                    foodModel.setAmount(foodAmount)
                    foodModel.setMeasurement(foodAmountMeasurement)
                    foodModel.setCaloriesAmount(foodCalories.toString())
                    foodModel.setCarbohydratesAmount(foodCarbohydrates)
                    foodModel.setFatsAmount(foodFats)
                    foodModel.setProteinsAmount(foodProteins)
                    foodModel.setMeal(intent?.getStringExtra("meal")!!)
                    historyFoodModelList.add(foodModel)
                }

                val layoutManager = LinearLayoutManager(context)
                val historyRecyclerView = historyRecyclerView
                historyRecyclerView.layoutManager = layoutManager
                val historyAdapter = AddFoodAdapter(historyFoodModelList, currentDate)
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
        val meal = intent.getStringExtra("meal").toString()
        val currentDate = intent.getStringExtra("date").toString()

        database = Firebase.database.reference

        val current = LocalDateTime.now()
        val formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss")
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
                val calories = input.toInt()
                mAuth.currentUser?.let {
                    database.child("users").child(it.uid).child("dates").child(currentDate)
                        .child("diary")
                        .child(meal).child("calories-$formattedTime").setValue(calories)
                }
                this.onBackPressed()
            } else {
                val snackbar = Snackbar.make(
                    historyRecyclerView, "Please input a number",
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    fun scanItem(item: MenuItem) {
        run {
            IntentIntegrator(this).initiateScan();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                println("OOOOOOOOO " + result.contents)
                addFoodInDatabase(result.contents)
            } else {
                println("SCAN FAILED")
                addBarcodeManuallyAlert()
            }
        }

        if (requestCode == addFoodRequestCode) {
            if (resultCode == RESULT_OK) {
                this.onBackPressed()
            }
        }
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
                addFoodInDatabase(input)
            } else {
                //input is empty
                val snackbar = Snackbar.make(
                    historyRecyclerView, "Please input a barcode",
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    private fun addFoodInDatabase(barcode: String) {
        database = Firebase.database.reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val meal = intent.getStringExtra("meal").toString()
                val currentDate = intent.getStringExtra("date").toString()

                val food = snapshot.child("food").child(barcode)
                if (food.exists()) {
                    val name = food.child("name").value.toString()
                    val description =
                        food.child("description").value.toString()
                    val calories =
                        food.child("calories").value.toString()
                    val carbohydrates =
                        food.child("carbohydrates").value.toString()
                    val fats = food.child("fats").value.toString()
                    val proteins =
                        food.child("proteins").value.toString()

                    val intent = Intent(context, FoodInfoActivity::class.java)
                    intent.putExtra("menu", "addFood")
                    intent.putExtra("id", barcode)
                    intent.putExtra("name", name)
                    intent.putExtra("description", description)
                    intent.putExtra("amount", "100")
                    intent.putExtra("caloriesAmount", calories)
                    intent.putExtra("carbohydratesAmount", carbohydrates)
                    intent.putExtra("fatsAmount", fats)
                    intent.putExtra("proteinsAmount", proteins)
                    intent.putExtra("meal", meal)
                    intent.putExtra("date", currentDate)
                    startActivityForResult(intent, addFoodRequestCode)
                } else {
                    val intent =
                        Intent(applicationContext, AddFoodInfoActivity::class.java)
                    intent.putExtra("barcode", barcode)
                    intent.putExtra("meal", meal)
                    intent.putExtra("currentDate", currentDate)
                    startActivityForResult(intent, addFoodRequestCode)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

//    private fun getAllFoodData(): ArrayList<FoodModel> {
//        val foodModelList = ArrayList<FoodModel>()
//
//        database = Firebase.database.reference
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                foodModelList.clear()
//
//                val foodListReference = mAuth.currentUser?.let {
//                    snapshot.child("food")
//                }
//
//                for (index in foodListReference?.children!!) {
//                    val meal = intent.getStringExtra("meal").toString()
//
//                    val foodId = index.key.toString()
//                    val foodName = index.child("name").value.toString()
//                    val foodDescription = index.child("description").value.toString()
//                    val foodAmountMeasurement = index.child("measurement").value.toString()
//                    val foodCalories = index.child("calories").value.toString()
//                    val foodCarbohydrates = index.child("carbohydrates").value.toString()
//                    val foodFats = index.child("fats").value.toString()
//                    val foodProteins = index.child("proteins").value.toString()
//
//                    val foodModel = FoodModel()
//                    foodModel.setName(foodName)
//                    foodModel.setId(foodId)
//                    foodModel.setDescription(foodDescription)
//                    foodModel.setAmount("100")
//                    foodModel.setMeasurement(foodAmountMeasurement)
//                    foodModel.setCaloriesAmount(foodCalories)
//                    foodModel.setCarbohydratesAmount(foodCarbohydrates)
//                    foodModel.setFatsAmount(foodFats)
//                    foodModel.setProteinsAmount(foodProteins)
//                    foodModel.setMeal(meal)
//                    foodModelList.add(foodModel)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//
//        return foodModelList
//    }
}