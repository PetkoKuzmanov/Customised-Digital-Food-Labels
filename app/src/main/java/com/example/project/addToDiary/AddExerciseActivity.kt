package com.example.project.addToDiary

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.project.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddExerciseActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)
        setSupportActionBar(findViewById(R.id.exerciseToolbar))

        val exercise = intent.getStringExtra("exercise")
        if (exercise != null) {
            val exerciseEditText = findViewById<EditText>(R.id.exerciseEditText)
            exerciseEditText.text =
                Editable.Factory.getInstance().newEditable(exercise)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuString = intent.getStringExtra("menu")

        if (menuString == "add") {
            menuInflater.inflate((R.menu.add_exercise_toolbar_layout), menu)
        } else if (menuString == "edit") {
            menuInflater.inflate((R.menu.exercise_edit_info_toolbar_layout), menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    fun addExerciseNext(item: MenuItem) {
        val exerciseEditText = findViewById<EditText>(R.id.exerciseEditText)
        val exercise = exerciseEditText.text.toString()

        if (exercise.isNotBlank()) {
            val currentDate = intent.getStringExtra("date").toString()
            val exerciseKey = intent.getStringExtra("key")

            val current = LocalDateTime.now()
            val formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss")
            val formattedTime = current.format(formatterTime)

            val databaseReference = mAuth.currentUser?.let {
                database.child("users").child(it.uid).child("dates").child(currentDate)
                    .child("diary")
                    .child("exercise")
            }

            if (exerciseKey == null) {
                databaseReference?.child("exercise-$formattedTime")
                    ?.setValue(exercise)
            } else {
                databaseReference?.child(exerciseKey)?.setValue(exerciseEditText.text.toString())
            }

            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            val snackbar = Snackbar.make(
                findViewById(R.id.addExerciseConstrainLayout), "Please input exercise",
                Snackbar.LENGTH_LONG
            )
            snackbar.show()
        }
    }

    fun exerciseDelete(item: MenuItem) {
        val foodKey = intent.getStringExtra("key").toString()
        val currentDate = intent.getStringExtra("date").toString()

        mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("dates").child(currentDate).child("diary")
                .child("exercise").child(foodKey).removeValue()
        }

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}