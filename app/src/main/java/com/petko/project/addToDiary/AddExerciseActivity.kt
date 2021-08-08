package com.petko.project.addToDiary

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.petko.project.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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

        val exercise = intent.getStringExtra(getString(R.string.exercise).toLowerCase())
        if (exercise != null) {
            val exerciseEditText = findViewById<EditText>(R.id.exerciseEditText)
            exerciseEditText.text =
                Editable.Factory.getInstance().newEditable(exercise)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuString = intent.getStringExtra(getString(R.string.menu).toLowerCase())

        if (menuString == getString(R.string.add).toLowerCase()) {
            menuInflater.inflate((R.menu.add_exercise_toolbar_layout), menu)
        } else if (menuString == getString(R.string.edit).toLowerCase()) {
            menuInflater.inflate((R.menu.exercise_edit_info_toolbar_layout), menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    fun addExerciseNext(item: MenuItem) {
        val exerciseEditText = findViewById<EditText>(R.id.exerciseEditText)
        val exercise = exerciseEditText.text.toString()

        if (exercise.isNotBlank()) {
            val currentDate =
                intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()
            val exerciseKey = intent.getStringExtra(getString(R.string.key).toLowerCase())

            val current = LocalDateTime.now()
            val formatterTime = DateTimeFormatter.ofPattern(getString(R.string.time_format))
            val formattedTime = current.format(formatterTime)

            val databaseReference = mAuth.currentUser?.let {
                database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                    .child(getString(R.string.dates).toLowerCase()).child(currentDate)
                    .child(getString(R.string.diary).toLowerCase())
                    .child(getString(R.string.exercise).toLowerCase())
            }

            if (exerciseKey == null) {
                databaseReference?.child(getString(R.string.exercise).toLowerCase() + "-$formattedTime")
                    ?.setValue(exercise)
            } else {
                databaseReference?.child(exerciseKey)?.setValue(exerciseEditText.text.toString())
            }

            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            val snackbar = Snackbar.make(
                findViewById(R.id.addExerciseConstrainLayout),
                getString(R.string.please_input_exercise),
                Snackbar.LENGTH_LONG
            )
            snackbar.show()
        }
    }

    fun exerciseDelete(item: MenuItem) {
        val foodKey = intent.getStringExtra(getString(R.string.key).toLowerCase()).toString()
        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase()).toString()

        mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid)
                .child(getString(R.string.dates).toLowerCase()).child(currentDate)
                .child(getString(R.string.diary).toLowerCase())
                .child(getString(R.string.exercise).toLowerCase()).child(foodKey).removeValue()
        }

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}