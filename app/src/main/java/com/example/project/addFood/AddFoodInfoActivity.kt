package com.example.project.addFood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.project.R

class AddFoodInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_info)
        setSupportActionBar(findViewById(R.id.addFoodInfoToolbar))
        println(intent.getStringExtra("barcode"))

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val measurementEditText = findViewById<EditText>(R.id.measurementEditText)
        val carbohydratesEditText = findViewById<EditText>(R.id.carbohydratesEditText)
        val fatsEditText = findViewById<EditText>(R.id.fatsEditText)
        val proteinsEditText = findViewById<EditText>(R.id.proteinsEditText)
        val caloriesTextView = findViewById<TextView>(R.id.addFoodCaloriesTextView)


        carbohydratesEditText.doAfterTextChanged {
            updateCalories(carbohydratesEditText.text, fatsEditText.text, proteinsEditText.text)
        }
        fatsEditText.doAfterTextChanged {
            updateCalories(carbohydratesEditText.text, fatsEditText.text, proteinsEditText.text)
        }
        proteinsEditText.doAfterTextChanged {
            updateCalories(carbohydratesEditText.text, fatsEditText.text, proteinsEditText.text)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.add_food_info_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    /*
    Update the textView for the calories
     */
    private fun updateCalories(
        carbohydrates: CharSequence,
        fats: CharSequence,
        proteins: CharSequence
    ) {
        val caloriesTextView = findViewById<TextView>(R.id.addFoodCaloriesTextView)

        var carbohydratesInt = 0
        var fatsInt = 0
        var proteinsInt = 0

        if (carbohydrates.isNotEmpty()) {
            carbohydratesInt = carbohydrates.toString().toInt() * 4
        }
        if (fats.isNotEmpty()) {
            fatsInt = fats.toString().toInt() * 9
        }
        if (proteins.isNotEmpty()) {
            proteinsInt = proteins.toString().toInt() * 4
        }

        val caloriesInt = carbohydratesInt + fatsInt + proteinsInt
        caloriesTextView.text = caloriesInt.toString()
    }

    fun addFoodInfo(item: MenuItem) {

    }
}