package com.example.nutrition

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_login)

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            login()
        }

        txtForgetPassword.setOnClickListener {
            forgotPassword()
        }

        txtSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun forgotPassword() {
        val emailfp = etLoginMailid.editText?.text.toString()
        if (emailfp.isEmpty()) {
            Toast.makeText(this, "Please enter mail id", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailfp)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset mail sent", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun login() {
        val email = etLoginMailid.editText?.text.toString()
        val password = etLoginPassword.editText?.text.toString()
        if (email == "") {
            Toast.makeText(this, "Please enter mail id", Toast.LENGTH_SHORT).show()
        } else if (password == "") {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        savePreferences()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error message:${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    fun savePreferences() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
}
