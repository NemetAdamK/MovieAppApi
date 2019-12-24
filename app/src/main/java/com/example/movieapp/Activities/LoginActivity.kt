package com.example.movieapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.movieapp.R
import com.example.movieapp.userId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        buttonSignUpUser.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

        buttonLogin.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        if (editTextLoginEmail.text.toString().isEmpty()) {
            editTextLoginEmail.error = "Please enter email"
            editTextLoginEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editTextLoginEmail.text.toString()).matches()) {
            editTextLoginEmail.error = "Please enter valid email"
            editTextLoginEmail.requestFocus()
            return
        }

        if (editTextLoginPassword.text.toString().isEmpty()) {
            editTextLoginPassword.error = "Please enter password"
            editTextLoginPassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(editTextLoginEmail.text.toString(), editTextLoginPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    userId = auth.currentUser!!.uid
                    FirebaseDatabase.getInstance().getReference("users").child(userId).child("name").setValue("No username")

                    updateUI(user)
                } else {

                    updateUI(null)
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?){

        if (currentUser != null) {
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(
                    baseContext, "Please verify your email address.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                baseContext, "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}
