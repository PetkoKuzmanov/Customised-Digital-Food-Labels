package com.example.project.addFood

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddFoodActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        setSupportActionBar(findViewById(R.id.addFoodToolBar))

    }

    fun quickAdd(view: View) {
        val inflater = LayoutInflater.from(this)
        val quickAddView = inflater.inflate(R.layout.quick_add_layout, null)
        val enterCalories = quickAddView.findViewById(R.id.enterCalories) as EditText
        val section = intent.getStringExtra("section").toString()

        database = Firebase.database.reference
        val databaseReference = mAuth.currentUser?.let {
            database.child("users").child(it.uid)
        }

        //Set the current date as the date
        val current = LocalDateTime.now()
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterTime = DateTimeFormatter.ofPattern("HH-mm-ss")

        val formattedDate = current.format(formatterDate)
        val formattedTime = current.format(formatterTime)

        //Create the alertDialog
        val builder = AlertDialog.Builder(this)
            .setView(quickAddView)
            .setTitle("Quick add")
        builder.create()
        builder.setPositiveButton("ADD") { _, _ ->
            val input = enterCalories.text.toString()


            //Check if the topic can be added
            if (!TextUtils.isEmpty(input)) {
//                mAdapter.addItem(weight, dateTextView.text.toString())
//                mAuth.currentUser?.let {
//                    database.child("users").child(it.uid).child(formattedDate).child("diary")
//                        .child(section).child(formattedTime).child("id").setValue(id)
//                }
                mAuth.currentUser?.let {
                    database.child("users").child(it.uid).child(formattedDate).child("diary")
                        .child(section).child("calories-$formattedTime").setValue(input)
                }

            }
        }
        builder.setNegativeButton("CANCEL") { _, _ ->
            //Cancels the adding of the weight even if this is left empty
        }
        builder.show()
    }

    fun scanItem(view: View) {

    }
}