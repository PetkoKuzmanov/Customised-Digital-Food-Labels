package com.example.project.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import com.example.project.R

class FoodInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_info)
        setSupportActionBar(findViewById(R.id.foodInfoToolbar))

        val foodNameTextView = findViewById<TextView>(R.id.foodNameTextView)
        val foodDescriptionTextView = findViewById<TextView>(R.id.foodDescriptionTextView)
        val numberOfServingsEditText = findViewById<EditText>(R.id.numberOfServingsEditText)
        val carbohydratesInfoAmount = findViewById<TextView>(R.id.carbohydratesInfoAmount)
        val fatsInfoAmount = findViewById<TextView>(R.id.fatsInfoAmount)
        val proteinsInfoAmount = findViewById<TextView>(R.id.proteinsInfoAmount)

        foodNameTextView.text = intent.getStringExtra("name")
        foodDescriptionTextView.text = intent.getStringExtra("description")
//        numberOfServingsEditText.text = intent.getStringExtra("description")
        carbohydratesInfoAmount.text = intent.getStringExtra("carbohydratesAmount")
        fatsInfoAmount.text = intent.getStringExtra("fatsAmount")
        proteinsInfoAmount.text = intent.getStringExtra("proteinsAmount")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.food_info_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun foodInfo(item: MenuItem) {

    }
}