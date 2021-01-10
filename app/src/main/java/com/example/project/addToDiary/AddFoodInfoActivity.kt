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
        val id = intent.getStringExtra("barcode")
        val meal = intent.getStringExtra("meal").toString()
        val currentDate = intent.getStringExtra("currentDate").toString()
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
            addSingleFoodInfo("name", name)
            addSingleFoodInfo("description", description)
            addSingleFoodInfo("measurement", measurement)
            addSingleFoodInfo("carbohydrates", carbohydratesText)
            addSingleFoodInfo("fats", fatsText)
            addSingleFoodInfo("proteins", proteinsText)
            addSingleFoodInfo("calories", caloriesText)

            //Open add food to diary
            val intent = Intent(this, FoodInfoActivity::class.java)
            intent.putExtra("menu", "addFood")
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("description", description)
            intent.putExtra("amount", "0")
            intent.putExtra("caloriesAmount", caloriesText.toString())
            intent.putExtra("carbohydratesAmount", carbohydratesText.toString())
            intent.putExtra("fatsAmount", fatsText.toString())
            intent.putExtra("proteinsAmount", proteinsText.toString())
            intent.putExtra("meal", meal)
            intent.putExtra("date", currentDate)
            ActivityCompat.startActivityForResult(this, intent, addFoodRequestCode, null)
        } else {
            val toast = Toast.makeText(
                applicationContext,
                "Please fill in the name, description and measurement",
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
        val id = intent.getStringExtra("barcode")
        mAuth.currentUser?.let {
            database.child("food").child(id!!).child(infoName).setValue(info)
        }
    }

    private fun addSingleFoodInfo(infoName: String, editable: Editable) {
        val id = intent.getStringExtra("barcode")

        var info = 0
        if (editable.isNotEmpty()) {
            info = editable.toString().toInt()
        }

        mAuth.currentUser?.let {
            database.child("food").child(id!!).child(infoName).setValue(info)
        }
    }
}