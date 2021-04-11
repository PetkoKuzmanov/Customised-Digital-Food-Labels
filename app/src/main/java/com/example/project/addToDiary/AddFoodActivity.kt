package com.example.project.addToDiary

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private var allFoodModelList = ArrayList<FoodModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(findViewById(R.id.addFoodToolbar))

        populateList()

        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()

        val searchView = findViewById<SearchView>(R.id.foodSearchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val historyAdapter =
                    AddFoodAdapter(historyFoodModelList, allFoodModelList, currentDate)
                historyRecyclerView.adapter = historyAdapter

                (historyRecyclerView.adapter as Filterable).filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val historyAdapter =
                    AddFoodAdapter(historyFoodModelList, historyFoodModelList, currentDate)
                historyRecyclerView.adapter = historyAdapter

                (historyRecyclerView.adapter as Filterable).filter.filter(newText)

                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.add_food_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                addFoodInDatabase(result.contents)
            } else {
                addBarcodeManuallyAlert()
            }
        }

        if (requestCode == addFoodRequestCode) {
            if (resultCode == RESULT_OK) {
                this.onBackPressed()
            }
        }
    }

    private fun populateList() {
        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()

        database = Firebase.database.reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyFoodModelList.clear()
                allFoodModelList.clear()

                val foodHistoryReference = mAuth.currentUser?.let {
                    snapshot.child(getString(R.string.users).toLowerCase()).child(it.uid)
                        .child(getString(R.string.history).toLowerCase())
                }

                for (index in foodHistoryReference?.children!!) {
                    val foodId = index.child(getString(R.string.id).toLowerCase()).value.toString()
                    val foodAmount =
                        index.child(getString(R.string.amount).toLowerCase()).value.toString()

                    val foodReference =
                        snapshot.child(getString(R.string.food).toLowerCase()).child(foodId)

                    val foodName =
                        foodReference.child(getString(R.string.name).toLowerCase()).value.toString()
                    val foodDescription =
                        foodReference.child(getString(R.string.description).toLowerCase()).value.toString()
                    val foodAmountMeasurement =
                        foodReference.child(getString(R.string.measurement).toLowerCase()).value.toString()
                    val foodCalories =
                        foodReference.child(getString(R.string.calories).toLowerCase()).value.toString()
                            .toInt()
                    val foodCarbohydrates =
                        foodReference.child(getString(R.string.carbohydrates).toLowerCase()).value.toString()
                    val foodFats =
                        foodReference.child(getString(R.string.fats).toLowerCase()).value.toString()
                    val foodProteins =
                        foodReference.child(getString(R.string.proteins).toLowerCase()).value.toString()

//                    val foodModel = FoodModel()
//                    foodModel.setName(foodName)
//                    foodModel.setId(foodId)
//                    foodModel.setDescription(foodDescription)
//                    foodModel.setAmount(foodAmount)
//                    foodModel.setMeasurement(foodAmountMeasurement)
//                    foodModel.setCaloriesAmount(foodCalories.toString())
//                    foodModel.setCarbohydratesAmount(foodCarbohydrates)
//                    foodModel.setFatsAmount(foodFats)
//                    foodModel.setProteinsAmount(foodProteins)
//                    foodModel.setMeal(intent?.getStringExtra(getString(R.string.meal).toLowerCase())!!)

                    val foodModel = FoodModel(
                        foodId,
                        foodName,
                        foodDescription,
                        foodAmount,
                        foodAmountMeasurement,
                        foodCalories.toString(),
                        foodCarbohydrates,
                        foodFats,
                        foodProteins,
                        intent?.getStringExtra(getString(R.string.meal).toLowerCase())!!,
                        index.key!!
                    )
                    historyFoodModelList.add(foodModel)
                }

                val foodDatabaseReference = snapshot.child("food")

                for (index in foodDatabaseReference.children) {
                    val foodId = index.key.toString()
                    val foodAmount = "100"

                    val foodName =
                        index.child(getString(R.string.name).toLowerCase()).value.toString()
                    val foodDescription =
                        index.child(getString(R.string.description).toLowerCase()).value.toString()
                    val foodAmountMeasurement =
                        index.child(getString(R.string.measurement).toLowerCase()).value.toString()
                    val foodCalories =
                        index.child(getString(R.string.calories).toLowerCase()).value.toString()
                            .toInt()
                    val foodCarbohydrates =
                        index.child(getString(R.string.carbohydrates).toLowerCase()).value.toString()
                    val foodFats =
                        index.child(getString(R.string.fats).toLowerCase()).value.toString()
                    val foodProteins =
                        index.child(getString(R.string.proteins).toLowerCase()).value.toString()

//                    val foodModel = FoodModel()
//                    foodModel.setName(foodName)
//                    foodModel.setId(foodId)
//                    foodModel.setDescription(foodDescription)
//                    foodModel.setAmount(foodAmount)
//                    foodModel.setMeasurement(foodAmountMeasurement)
//                    foodModel.setCaloriesAmount(foodCalories.toString())
//                    foodModel.setCarbohydratesAmount(foodCarbohydrates)
//                    foodModel.setFatsAmount(foodFats)
//                    foodModel.setProteinsAmount(foodProteins)
//                    foodModel.setMeal(intent?.getStringExtra(getString(R.string.meal).toLowerCase())!!)

                    val foodModel = FoodModel(
                        foodId,
                        foodName,
                        foodDescription,
                        foodAmount,
                        foodAmountMeasurement,
                        foodCalories.toString(),
                        foodCarbohydrates,
                        foodFats,
                        foodProteins,
                        intent?.getStringExtra(getString(R.string.meal).toLowerCase())!!,
                        index.key!!
                    )
                    allFoodModelList.add(foodModel)
                }

                val layoutManager = LinearLayoutManager(context)
                val historyRecyclerView = historyRecyclerView
                historyRecyclerView.layoutManager = layoutManager
                val historyAdapter =
                    AddFoodAdapter(historyFoodModelList, allFoodModelList, currentDate)
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
        val meal = intent.getStringExtra(getString(R.string.meal).toLowerCase()).toString()
        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()

        database = Firebase.database.reference

        val current = LocalDateTime.now()
        val formatterTime = DateTimeFormatter.ofPattern(getString(R.string.time_format))
        val formattedTime = current.format(formatterTime)

        //Create the alertDialog
        val builder = AlertDialog.Builder(this)
            .setView(quickAddView)
            .setTitle(getString(R.string.quick_add))
        builder.create()
        builder.setPositiveButton(getString(R.string.add).toUpperCase()) { _, _ ->
            val input = enterCalories.text.toString()

            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
                val calories = input.toInt()
                mAuth.currentUser?.let {
                    database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                        .child(getString(R.string.dates).toLowerCase()).child(currentDate)
                        .child(getString(R.string.diary).toLowerCase())
                        .child(meal)
                        .child(getString(R.string.calories).toLowerCase() + "-$formattedTime")
                        .setValue(calories)
                }
                this.onBackPressed()
            } else {
                val snackbar = Snackbar.make(
                    historyRecyclerView, getString(R.string.please_input_a_number),
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            }
        }
        builder.setNegativeButton(getString(R.string.cancel).toUpperCase()) { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    fun scanItem(item: MenuItem) {
        run {
            IntentIntegrator(this).initiateScan()
        }
    }

    private fun addBarcodeManuallyAlert() {
        val inflater = LayoutInflater.from(this)
        val addBarcodeView = inflater.inflate(R.layout.add_barcode_manually_layout, null)
        val enterBarcode = addBarcodeView.findViewById(R.id.enterBarcode) as EditText

        //Create the alertDialog
        val builder = AlertDialog.Builder(this)
            .setView(addBarcodeView)
            .setTitle(getString(R.string.could_not_find_barcode))
        builder.create()
        builder.setPositiveButton(getString(R.string.add).toUpperCase()) { _, _ ->
            val input = enterBarcode.text.toString()


            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
                addFoodInDatabase(input)
            } else {
                //input is empty
                val snackbar = Snackbar.make(
                    historyRecyclerView, getString(R.string.please_input_a_barcode),
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            }
        }
        builder.setNegativeButton(getString(R.string.calories).toUpperCase()) { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    private fun addFoodInDatabase(barcode: String) {
        database = Firebase.database.reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val meal = intent.getStringExtra(getString(R.string.meal).toLowerCase()).toString()
                val currentDate =
                    intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()

                val food = snapshot.child(getString(R.string.food).toLowerCase()).child(barcode)
                if (food.exists()) {
                    val name = food.child(getString(R.string.name).toLowerCase()).value.toString()
                    val description =
                        food.child(getString(R.string.description).toLowerCase()).value.toString()
                    val calories =
                        food.child(getString(R.string.calories).toLowerCase()).value.toString()
                    val carbohydrates =
                        food.child(getString(R.string.carbohydrates).toLowerCase()).value.toString()
                    val fats = food.child(getString(R.string.fats).toLowerCase()).value.toString()
                    val proteins =
                        food.child(getString(R.string.proteins).toLowerCase()).value.toString()

                    val intent = Intent(context, FoodInfoActivity::class.java)
                    intent.putExtra(
                        getString(R.string.menu).toLowerCase(),
                        getString(R.string.addFood)
                    )
                    intent.putExtra(getString(R.string.id).toLowerCase(), barcode)
                    intent.putExtra(getString(R.string.name).toLowerCase(), name)
                    intent.putExtra(getString(R.string.description).toLowerCase(), description)
                    intent.putExtra(
                        getString(R.string.amount).toLowerCase(),
                        getString(R.string.one_hundred).toLowerCase()
                    )
                    intent.putExtra(getString(R.string.caloriesAmount), calories)
                    intent.putExtra(getString(R.string.carbohydratesAmount), carbohydrates)
                    intent.putExtra(getString(R.string.fatsAmount), fats)
                    intent.putExtra(getString(R.string.proteinsAmount), proteins)
                    intent.putExtra(getString(R.string.meal).toLowerCase(), meal)
                    intent.putExtra(getString(R.string.date).toLowerCase(), currentDate)
                    startActivityForResult(intent, addFoodRequestCode)
                } else {
                    val intent =
                        Intent(applicationContext, AddFoodInfoActivity::class.java)
                    intent.putExtra(getString(R.string.barcode).toLowerCase(), barcode)
                    intent.putExtra(getString(R.string.meal).toLowerCase(), meal)
                    intent.putExtra(getString(R.string.currentDate), currentDate)
                    startActivityForResult(intent, addFoodRequestCode)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}