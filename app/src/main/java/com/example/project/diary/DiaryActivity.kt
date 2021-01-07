package com.example.project.diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.project.MainActivity
import com.example.project.R
import com.example.project.macronutrients.MacronutrientsActivity
import com.example.project.weight.WeightActivity
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DiaryActivity : AppCompatActivity() {
    private lateinit var currentDate: LocalDateTime
    private lateinit var formatterDate: DateTimeFormatter
    private lateinit var formattedDate: String

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val mToolbar = findViewById<Toolbar>(R.id.diaryToolbar)
        setSupportActionBar(mToolbar)

        currentDate = LocalDateTime.now()
        formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        formattedDate = currentDate.format(formatterDate)

        setFragment()
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

    fun previousDay(view: View) {
        currentDate = currentDate.minusDays(1)
        formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        formattedDate = currentDate.format(formatterDate)

        println("AAAAAAAAAAAAAAAAAAAAAAAAAA " + formattedDate)
        setFragment()
    }

    fun nextDay(view: View) {
        currentDate = currentDate.plusDays(1)
        formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        formattedDate = currentDate.format(formatterDate)

        println("BBBBBBBBBBBBBBBBBBBBBB " + formattedDate)
        setFragment()
    }

    private fun setFragment() {
        val fragment = DiaryDayFragment(formattedDate)
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}