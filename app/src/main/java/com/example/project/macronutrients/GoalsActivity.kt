package com.example.project.macronutrients

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GoalsActivity : AppCompatActivity(){

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        setSupportActionBar(findViewById(R.id.macronutrientsToolbar))

        database = Firebase.database.reference

        mAuth.currentUser?.let { database.child("users").child(it.uid).child("goals").child("carbohydrates").setValue("444") }
        mAuth.currentUser?.let { database.child("users").child(it.uid).child("goals").child("fats").setValue("111") }
        mAuth.currentUser?.let { database.child("users").child(it.uid).child("goals").child("proteins").setValue("222") }


        
    }
}