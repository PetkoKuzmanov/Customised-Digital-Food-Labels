package com.petko.project.diary

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.petko.project.MainActivity
import com.petko.project.R
import com.petko.project.macronutrients.MacronutrientsActivity
import com.petko.project.weight.WeightActivity
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DiaryActivity : AppCompatActivity() {
    private lateinit var currentDate: LocalDateTime
    private lateinit var formattedDate: String

    private val formatterDate: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val mToolbar = findViewById<Toolbar>(R.id.diaryToolbar)
        setSupportActionBar(mToolbar)
        mToolbar.setTitleTextColor(Color.WHITE)

        currentDate = LocalDateTime.now()
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
                    baseContext, getString(R.string.signing_out),
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
        formattedDate = currentDate.format(formatterDate)

        setFragment()
    }

    fun nextDay(view: View) {
        currentDate = currentDate.plusDays(1)
        formattedDate = currentDate.format(formatterDate)

        setFragment()
    }

    @SuppressLint("SetTextI18n")
    private fun setFragment() {
        val dateTextView = findViewById<TextView>(R.id.dateTextView)

        val todayDate = LocalDateTime.now()
        val yesterdayDate = todayDate.minusDays(1)
        val tomorrowDate = todayDate.plusDays(1)

        val formattedTodayDate = todayDate.format(formatterDate)
        val formattedYesterdayDate = yesterdayDate.format(formatterDate)
        val formattedTomorrowDate = tomorrowDate.format(formatterDate)

        when (formattedDate) {
            formattedTodayDate -> {
                dateTextView.text = getString(R.string.today)
            }
            formattedYesterdayDate -> {
                dateTextView.text = getString(R.string.yesterday)
            }
            formattedTomorrowDate -> {
                dateTextView.text = getString(R.string.tomorrow)
            }
            else -> {
                val formatterDateForTextView: DateTimeFormatter =
                    DateTimeFormatter.ofPattern(getString(R.string.date_for_text_view))

                dateTextView.text = currentDate.dayOfWeek.toString().toLowerCase()
                    .capitalize() + ", " + currentDate.format(formatterDateForTextView)
            }
        }

        val fragment = DiaryDayFragment(formattedDate)
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val closeAppIntent = Intent(Intent.ACTION_MAIN)
        closeAppIntent.addCategory(Intent.CATEGORY_HOME)
        closeAppIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(closeAppIntent)
    }
}