package com.petko.project.macronutrients

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.petko.project.R
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

        //Get the edit and text views
        val carbohydratesGoalAmount = findViewById<EditText>(R.id.carbohydratesEditText)
        val fatsGoalAmount = findViewById<EditText>(R.id.fatsEditText)
        val proteinsGoalAmount = findViewById<EditText>(R.id.proteinsEditText)

        val carbohydratesCalories = findViewById<TextView>(R.id.carbohydratesCalories)
        val fatsCalories = findViewById<TextView>(R.id.fatsCalories)
        val proteinsCalories = findViewById<TextView>(R.id.proteinsCalories)

        val carbohydrates =
            intent.getStringExtra(getString(R.string.carbohydrates).toLowerCase()).toString()
        val fats = intent.getStringExtra(getString(R.string.fats).toLowerCase()).toString()
        val proteins = intent.getStringExtra(getString(R.string.proteins).toLowerCase()).toString()

        //Set the texts of the edit and text views if the user already has a goal
        carbohydratesGoalAmount.setText(carbohydrates, TextView.BufferType.EDITABLE)
        updateCaloriesForMacronutrient(carbohydratesCalories, carbohydratesGoalAmount.text)

        fatsGoalAmount.setText(fats, TextView.BufferType.EDITABLE)
        updateCaloriesForMacronutrient(fatsCalories, fatsGoalAmount.text)

        proteinsGoalAmount.setText(proteins, TextView.BufferType.EDITABLE)
        updateCaloriesForMacronutrient(proteinsCalories, proteinsGoalAmount.text)

        updateCalories(carbohydratesCalories.text, fatsCalories.text, proteinsCalories.text)

        //Event listeners which update the data when the user inputs amounts for macronutrients
        carbohydratesGoalAmount.doAfterTextChanged { editable ->
            if (editable?.isNotEmpty()!!) {
                updateCaloriesForMacronutrient(carbohydratesCalories, editable)
            } else {
                carbohydratesCalories.text = getString(R.string.zero)
            }
            updateCalories(carbohydratesCalories.text, fatsCalories.text, proteinsCalories.text)
        }

        fatsGoalAmount.doAfterTextChanged { editable ->
            if (editable?.isNotEmpty()!!) {
                updateCaloriesForMacronutrient(fatsCalories, editable)
            } else {
                fatsCalories.text = getString(R.string.zero)
            }
            updateCalories(carbohydratesCalories.text, fatsCalories.text, proteinsCalories.text)
        }

        proteinsGoalAmount.doAfterTextChanged { editable ->
            if (editable?.isNotEmpty()!!) {
                updateCaloriesForMacronutrient(proteinsCalories, editable)
            } else {
                proteinsCalories.text = getString(R.string.zero)
            }
            updateCalories(carbohydratesCalories.text, fatsCalories.text, proteinsCalories.text)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.goals_toolbar_layout), menu)
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
        val caloriesGoalAmountTextView = findViewById<TextView>(R.id.caloriesAmountTextView)

        val carbohydratesInt = carbohydrates.toString().toInt()
        val fatsInt = fats.toString().toInt()
        val proteinsInt = proteins.toString().toInt()

        val caloriesInt = carbohydratesInt + fatsInt + proteinsInt
        caloriesGoalAmountTextView.text = caloriesInt.toString()
    }

    private fun updateCaloriesForMacronutrient(
        textView: TextView,
        macronutrientEditable: Editable
    ) {
        if (resources.getResourceEntryName(textView.id) == getString(R.string.fatsCalories)) {
            textView.text = (macronutrientEditable.toString().toInt() * 9).toString()
        } else {
            textView.text = (macronutrientEditable.toString().toInt() * 4).toString()
        }
    }

    private fun addMacronutrientsToDatabase(macronutrient: String, amount: Int) {
        mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                .child(getString(R.string.goals).toLowerCase()).child(macronutrient)
                .setValue(amount)
        }
    }

    fun addGoals(item: MenuItem) {
        val carbohydratesCalories = findViewById<TextView>(R.id.carbohydratesCalories)
        val fatsCalories = findViewById<TextView>(R.id.fatsCalories)
        val proteinsCalories = findViewById<TextView>(R.id.proteinsCalories)
        val caloriesGoalAmountTextView = findViewById<TextView>(R.id.caloriesAmountTextView)

        val carbohydratesInt = carbohydratesCalories.text.toString().toInt() / 4
        val fatsInt = fatsCalories.text.toString().toInt() / 9
        val proteinsInt = proteinsCalories.text.toString().toInt() / 4
        val caloriesInt = caloriesGoalAmountTextView.text.toString().toInt()

        mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                .child(getString(R.string.goals).toLowerCase())
                .child(getString(R.string.carbohydrates).toLowerCase())
                .setValue(carbohydratesInt)
        }

        mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                .child(getString(R.string.goals).toLowerCase())
                .child(getString(R.string.fats).toLowerCase())
                .setValue(fatsInt)
        }

        mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                .child(getString(R.string.goals).toLowerCase())
                .child(getString(R.string.proteins).toLowerCase())
                .setValue(proteinsInt)
        }

        mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                .child(getString(R.string.goals).toLowerCase())
                .child(getString(R.string.calories).toLowerCase())
                .setValue(caloriesInt)
        }
        this.onBackPressed()
    }
}