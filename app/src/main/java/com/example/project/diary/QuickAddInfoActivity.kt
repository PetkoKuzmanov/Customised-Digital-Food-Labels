package com.example.project.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QuickAddInfoActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_add_info)
        setSupportActionBar(findViewById(R.id.quickAddInfoToolbar))

        val quickAddEditText = findViewById<EditText>(R.id.quickAddEditText)
        val caloriesAmount = intent.getStringExtra(getString(R.string.caloriesAmount)).toString().toInt()

        quickAddEditText.text =
            Editable.Factory.getInstance().newEditable(caloriesAmount.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.quick_add_edit_info_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun quickAddInfoNext(item: MenuItem) {
        val quickAddEditText = findViewById<EditText>(R.id.quickAddEditText)
        database = Firebase.database.reference

        val foodKey = intent.getStringExtra(getString(R.string.key).toLowerCase())
        val meal = intent.getStringExtra(getString(R.string.meal).toLowerCase())
        val calories = quickAddEditText.text.toString().toInt()
        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase())

         mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid).child(getString(R.string.dates).toLowerCase()).child(currentDate!!).child(getString(R.string.diary).toLowerCase())
                .child(meal!!).child(foodKey!!).setValue(calories)
        }
        this.onBackPressed()
    }

    fun quickAddDelete(item: MenuItem) {
        database = Firebase.database.reference
        val meal = intent.getStringExtra(getString(R.string.meal).toLowerCase()).toString()
        val foodKey = intent.getStringExtra(getString(R.string.key).toLowerCase()).toString()
        val currentDate = intent.getStringExtra(getString(R.string.date).toLowerCase())

        mAuth.currentUser?.let {
            database.child(getString(R.string.users).toLowerCase()).child(it.uid).child(getString(R.string.dates).toLowerCase()).child(currentDate!!).child(getString(R.string.diary).toLowerCase())
                .child(meal).child(foodKey).removeValue()
        }
        this.onBackPressed()
    }
}