package com.example.movieapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.movieapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        buttonSignUp.setOnClickListener{
            signUpUser()
        }
    }

    private fun signUpUser() {
        if (editTextSignUpEmail.text.toString().isEmpty() ){
            editTextSignUpEmail.error = "Please enter email"
            editTextSignUpEmail.requestFocus()
            return
        }

        if (editTextSignUpPassword.text.toString().isEmpty()){
            editTextSignUpPassword.error = "Please enter password"
            editTextSignUpPassword.requestFocus()
            return

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editTextSignUpEmail.text.toString()).matches()) {
            editTextSignUpEmail.error = "Please enter valid email"
            editTextSignUpEmail.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(editTextSignUpEmail.text.toString(), editTextSignUpPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
