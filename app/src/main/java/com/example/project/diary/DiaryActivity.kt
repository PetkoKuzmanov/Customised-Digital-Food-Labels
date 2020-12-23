package com.example.project.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.project.MainActivity
import com.example.project.R
import com.example.project.macronutrients.MacronutrientsActivity
import com.example.project.weight.WeightActivity
import com.github.mikephil.charting.charts.PieChart
import com.google.firebase.auth.FirebaseAuth

class DiaryActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val mToolbar = findViewById<PieChart>(R.id.diaryToolbar) as Toolbar
        setSupportActionBar(mToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_macronutrients -> {
                val intent = Intent(applicationContext, MacronutrientsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_weight -> {
                val intent = Intent(applicationContext, WeightActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                Toast.makeText(
                    baseContext, "Signing out.",
                    Toast.LENGTH_SHORT
                ).show()
                mAuth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}