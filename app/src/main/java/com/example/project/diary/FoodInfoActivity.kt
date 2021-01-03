package com.example.project.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.project.R
import kotlin.math.roundToInt

class FoodInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_info)
        setSupportActionBar(findViewById(R.id.foodInfoToolbar))

        val foodNameTextView = findViewById<TextView>(R.id.foodNameTextView)
        val foodDescriptionTextView = findViewById<TextView>(R.id.foodDescriptionTextView)
        val numberOfServingsEditText = findViewById<EditText>(R.id.numberOfServingsEditText)

        val foodAmount = intent.getStringExtra("amount").toString().toInt()

        foodNameTextView.text = intent.getStringExtra("name")
        foodDescriptionTextView.text = intent.getStringExtra("description")
        numberOfServingsEditText.text = Editable.Factory.getInstance().newEditable(foodAmount.toString())

        updateMacronutrients(numberOfServingsEditText.text)

        numberOfServingsEditText.doAfterTextChanged { editable ->
            if (editable?.isNotEmpty()!!) {
                updateMacronutrients(editable)
            } else {
                updateMacronutrientsToZero()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.food_info_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun foodInfo(item: MenuItem) {

    }

    private fun updateMacronutrients(amountEditable: Editable) {
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        val foodAmount = amountEditable.toString().toDouble()
        val carbohydratesAmount = (intent.getStringExtra("carbohydratesAmount")
            ?.toInt()!! * foodAmount / 100).roundToInt().toString()
        val fatsAmount =
            (intent.getStringExtra("fatsAmount")?.toInt()!! * foodAmount / 100).roundToInt()
                .toString()
        val proteinsAmount =
            (intent.getStringExtra("proteinsAmount")?.toInt()!! * foodAmount / 100).roundToInt()
                .toString()

        carbohydratesInfoAmount.text = carbohydratesAmount + "g"
        fatsInfoAmount.text = fatsAmount + "g"
        proteinsInfoAmount.text = proteinsAmount + "g"
    }

    private fun updateMacronutrientsToZero() {
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        carbohydratesInfoAmount.text = "0g"
        fatsInfoAmount.text = "0g"
        proteinsInfoAmount.text = "0g"
    }
}