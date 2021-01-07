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
        val caloriesAmount = intent.getStringExtra("caloriesAmount").toString().toInt()

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

        val foodKey = intent.getStringExtra("key")
        val meal = intent.getStringExtra("meal")
        val calories = quickAddEditText.text.toString().toInt()

        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val formattedDate = current.format(formatterDate)

         mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("dates").child(formattedDate).child("diary")
                .child(meal!!).child(foodKey!!)?.setValue(calories)
        }
        this.onBackPressed()
    }

    fun quickAddDelete(item: MenuItem) {
        database = Firebase.database.reference
        val meal = intent.getStringExtra("meal").toString()
        val foodKey = intent.getStringExtra("key").toString()

        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = current.format(formatterDate)

        mAuth.currentUser?.let {
            database.child("users").child(it.uid).child("dates").child(formattedDate).child("diary")
                .child(meal).child(foodKey).removeValue()
        }
        this.onBackPressed()
    }
}