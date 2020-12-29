package com.example.nutrition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    var firebaseUserID: String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        btnRegister.setOnClickListener {
            performRegister()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun performRegister() {
        val name = etName.editText?.text.toString()
        val email = etEmail.editText?.text.toString()
        val mobileno = etMobileno.editText?.text.toString()
        val rpassword = etRegisterPassword.editText?.text.toString()
        val cpassword = etConfirmPassword.editText?.text.toString()
        if (email.isEmpty() || rpassword.isEmpty() || name.isEmpty() || mobileno.isEmpty() || cpassword.isEmpty()) {
            Toast.makeText(this, "Please enter the credentials", Toast.LENGTH_LONG).show()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email Address", Toast.LENGTH_LONG).show()
            return
        } else if (rpassword != cpassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
            return
        } else if (rpassword.length < 6) {
            Toast.makeText(this, "Password too short", Toast.LENGTH_LONG).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, rpassword)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
                    mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
                        .child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["Name"] = name
                    userHashMap["Email"] = email
                    userHashMap["Mobile Number"] = mobileno
                    mDatabase.updateChildren(userHashMap)
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
            }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()

                }

            }
    }
}
