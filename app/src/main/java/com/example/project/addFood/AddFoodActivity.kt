package com.example.project.addFood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project.R

class AddFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(findViewById(R.id.addFoodToolBar))

    }
}