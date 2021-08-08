package com.petko.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.petko.project.diary.DiaryActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailText = findViewById<EditText>(R.id.email)
        val passwordText = findViewById<EditText>(R.id.password)

        val buttonLogIn = findViewById<Button>(R.id.buttonLogIn)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonLogIn.setOnClickListener { view ->
            signIn(view, emailText.text.toString(), passwordText.text.toString())
        }
        buttonRegister.setOnClickListener { view ->
            registerAccount(view, emailText.text.toString(), passwordText.text.toString())
        }

    }

    override fun onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, DiaryActivity::class.java)
//            intent.putExtra("user", user?.email)
            startActivity(intent)
        }
    }

    private fun signIn(view: View, email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        closeKeyboard()
                        showMessage(view, getString(R.string.cant_log_in))
                    }
                }
        } else {
            closeKeyboard()
            showMessage(view, getString(R.string.please_fill_in_all_fields))
        }
    }

    private fun registerAccount(view: View, email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        closeKeyboard()
                        showMessage(view, getString(R.string.cant_register))
                    }
                }
        } else {
            closeKeyboard()
            showMessage(view, getString(R.string.please_fill_in_all_fields))
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}