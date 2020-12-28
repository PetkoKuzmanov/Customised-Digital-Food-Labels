package com.example.project.addFood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project.R

class AddFoodInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_info)
        setSupportActionBar(findViewById(R.id.addFoodInfoToolbar))
        println(intent.getStringExtra("barcode"))
    }
}