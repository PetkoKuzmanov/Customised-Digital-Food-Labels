package com.example.project.macronutrients

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GoalsActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        setSupportActionBar(findViewById(R.id.macronutrientsToolbar))

        database = Firebase.database.reference

//        mAuth.currentUser?.let {
//            database.child("users").child(it.uid).child("goals").child("carbohydrates")
//                .setValue("444")
//        }
//        mAuth.currentUser?.let {
//            database.child("users").child(it.uid).child("goals").child("fats").setValue("111")
//        }
//        mAuth.currentUser?.let {
//            database.child("users").child(it.uid).child("goals").child("proteins").setValue("222")
//        }

        val caloriesGoalAmountTextView = findViewById<TextView>(R.id.caloriesGoalAmountTextView)

        val carbohydratesGoalAmount = findViewById<EditText>(R.id.carbohydratesGoalAmount)
        val carbohydratesCalories = findViewById<TextView>(R.id.carbohydratesCalories)

        val fatsGoalAmount = findViewById<EditText>(R.id.fatsGoalAmount)
        val fatsCalories = findViewById<TextView>(R.id.fatsCalories)

        val proteinsGoalAmount = findViewById<EditText>(R.id.proteinsGoalAmount)
        val proteinsCalories = findViewById<TextView>(R.id.proteinsCalories)

        carbohydratesGoalAmount.doAfterTextChanged {
            carbohydratesCalories.text = (it.toString().toInt() * 4).toString()
            caloriesGoalAmountTextView.text =
                (carbohydratesCalories.text.toString().toInt() + fatsCalories.text.toString()
                    .toInt() + proteinsCalories.text.toString().toInt()).toString()
        }

        fatsGoalAmount.doAfterTextChanged {
            fatsCalories.text = (it.toString().toInt() * 9).toString()
            caloriesGoalAmountTextView.text =
                (carbohydratesCalories.text.toString().toInt() + fatsCalories.text.toString()
                    .toInt() + proteinsCalories.text.toString().toInt()).toString()
        }


        proteinsGoalAmount.doAfterTextChanged {
            proteinsCalories.text = (it.toString().toInt() * 4).toString()
            caloriesGoalAmountTextView.text =
                (carbohydratesCalories.text.toString().toInt() + fatsCalories.text.toString()
                    .toInt() + proteinsCalories.text.toString().toInt()).toString()
        }
    }
}