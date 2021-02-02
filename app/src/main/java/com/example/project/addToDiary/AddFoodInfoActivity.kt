package com.example.project.addToDiary

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.project.R
import com.example.project.diary.FoodInfoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddFoodInfoActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    private val addFoodRequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_info)
        setSupportActionBar(findViewById(R.id.addFoodInfoToolbar))

        database = Firebase.database.reference
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.add_food_info_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun addFoodInfo(item: MenuItem) {
        val id = intent.getStringExtra(getString(R.string.barcode).toLowerCase())
        val meal = intent.getStringExtra(getString(R.string.meal).toLowerCase()).toString()
        val currentDate = intent.getStringExtra(getString(R.string.currentDate)).toString()
        val name = findViewById<EditText>(R.id.nameEditText).text.toString()
        val description = findViewById<EditText>(R.id.descriptionEditText).text.toString()
        val measurement = findViewById<EditText>(R.id.measurementEditText).text.toString()
        val carbohydratesText = findViewById<EditText>(R.id.carbohydratesEditText).text
        val fatsText = findViewById<EditText>(R.id.fatsEditText).text
        val proteinsText = findViewById<EditText>(R.id.proteinsEditText).text
        val caloriesText = findViewById<EditText>(R.id.addFoodCaloriesEditText).text

        //Add the food to the database if the name, description and measurement are not empty
        if (name.isNotEmpty()
            && description.isNotEmpty()
            && measurement.isNotEmpty()
        ) {
            addSingleFoodInfo(getString(R.string.name).toLowerCase(), name)
            addSingleFoodInfo(getString(R.string.description).toLowerCase(), description)
            addSingleFoodInfo(getString(R.string.measurement).toLowerCase(), measurement)
            addSingleFoodInfo(getString(R.string.carbohydrates).toLowerCase(), carbohydratesText)
            addSingleFoodInfo(getString(R.string.fats).toLowerCase(), fatsText)
            addSingleFoodInfo(getString(R.string.proteins).toLowerCase(), proteinsText)
            addSingleFoodInfo(getString(R.string.calories).toLowerCase(), caloriesText)

            //Open add food to diary
            val intent = Intent(this, FoodInfoActivity::class.java)
            intent.putExtra(getString(R.string.menu).toLowerCase(), getString(R.string.addFood))
            intent.putExtra(getString(R.string.id).toLowerCase(), id)
            intent.putExtra(getString(R.string.name).toLowerCase(), name)
            intent.putExtra(getString(R.string.description).toLowerCase(), description)
            intent.putExtra(getString(R.string.amount).toLowerCase(), getString(R.string.zero))
            intent.putExtra(getString(R.string.caloriesAmount), caloriesText.toString())
            intent.putExtra(getString(R.string.carbohydratesAmount), carbohydratesText.toString())
            intent.putExtra(getString(R.string.fatsAmount), fatsText.toString())
            intent.putExtra(getString(R.string.proteinsAmount), proteinsText.toString())
            intent.putExtra(getString(R.string.meal).toLowerCase(), meal)
            intent.putExtra(getString(R.string.date).toLowerCase(), currentDate)
            ActivityCompat.startActivityForResult(this, intent, addFoodRequestCode, null)
        } else {
            val toast = Toast.makeText(
                applicationContext,
                getString(R.string.please_fill_name_description_measurement),
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == addFoodRequestCode) {
            if (resultCode == RESULT_OK) {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun addSingleFoodInfo(infoName: String, info: String) {
        val id = intent.getStringExtra(getString(R.string.barcode).toLowerCase())
        mAuth.currentUser?.let {
            database.child(getString(R.string.food).toLowerCase()).child(id!!).child(infoName)
                .setValue(info)
        }
    }

    private fun addSingleFoodInfo(infoName: String, editable: Editable) {
        val id = intent.getStringExtra(getString(R.string.barcode).toLowerCase())

        var info = 0
        if (editable.isNotEmpty()) {
            info = editable.toString().toInt()
        }

        mAuth.currentUser?.let {
            database.child(getString(R.string.food).toLowerCase()).child(id!!).child(infoName)
                .setValue(info)
        }
    }
}