package com.example.project.addFood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddFoodInfoActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_info)
        setSupportActionBar(findViewById(R.id.addFoodInfoToolbar))

        database = Firebase.database.reference

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

        var carbohydratesCalories = 0
        var fatsCalories = 0
        var proteinsCalories = 0

        if (carbohydrates.isNotEmpty()) {
            carbohydratesCalories = carbohydrates.toString().toInt() * 4
        }
        if (fats.isNotEmpty()) {
            fatsCalories = fats.toString().toInt() * 9
        }
        if (proteins.isNotEmpty()) {
            proteinsCalories = proteins.toString().toInt() * 4
        }

        val caloriesInt = carbohydratesCalories + fatsCalories + proteinsCalories
        caloriesTextView.text = caloriesInt.toString()
    }

    fun addFoodInfo(item: MenuItem) {
        val id = intent.getStringExtra("barcode")
        val name = findViewById<EditText>(R.id.nameEditText).text.toString()
        val description = findViewById<EditText>(R.id.descriptionEditText).text.toString()
        val measurement = findViewById<EditText>(R.id.measurementEditText).text.toString()
        val carbohydrates = findViewById<EditText>(R.id.carbohydratesEditText).text
        val fats = findViewById<EditText>(R.id.fatsEditText).text
        val proteins = findViewById<EditText>(R.id.proteinsEditText).text
        val calories = findViewById<TextView>(R.id.addFoodCaloriesTextView).text.toString().toInt()

        //If a field is empty set it to zero
        var carbohydratesInt = 0
        var fatsInt = 0
        var proteinsInt = 0

        if (carbohydrates.isNotEmpty()) {
            carbohydratesInt = carbohydrates.toString().toInt()
        }
        if (fats.isNotEmpty()) {
            fatsInt = fats.toString().toInt()
        }
        if (proteins.isNotEmpty()) {
            proteinsInt = proteins.toString().toInt()
        }

        //Add the food to the database if the name, description and measurement are not empty
        if (name.isNotEmpty()
            && description.isNotEmpty()
            && measurement.isNotEmpty()
        ) {
            mAuth.currentUser?.let {
                database.child("food").child(id!!).child("name").setValue(name)
            }

            mAuth.currentUser?.let {
                database.child("food").child(id!!).child("description").setValue(description)
            }

            mAuth.currentUser?.let {
                database.child("food").child(id!!).child("measurement").setValue(measurement)
            }

            mAuth.currentUser?.let {
                database.child("food").child(id!!).child("carbohydrates").setValue(carbohydratesInt)
            }

            mAuth.currentUser?.let {
                database.child("food").child(id!!).child("fats").setValue(fatsInt)
            }

            mAuth.currentUser?.let {
                database.child("food").child(id!!).child("proteins").setValue(proteinsInt)
            }

            mAuth.currentUser?.let {
                database.child("food").child(id!!).child("calories").setValue(calories)
            }
            this.onBackPressed()
        } else {
            val toast = Toast.makeText(
                applicationContext,
                "Please fill in the name, description and measurement",
                Toast.LENGTH_LONG
            )
            toast.show()
        }
    }
}