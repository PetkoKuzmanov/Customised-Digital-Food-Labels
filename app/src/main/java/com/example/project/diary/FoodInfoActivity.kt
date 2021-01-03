package com.example.project.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.project.R

class FoodInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_info)
        setSupportActionBar(findViewById(R.id.foodInfoToolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.food_info_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun foodInfo(item: MenuItem) {

    }
}