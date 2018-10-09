package com.solution.grace.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        sign_in_button.setOnClickListener {
            loginUser()
        }

        back_sign_in_textView.setOnClickListener {
            finish()
        }
    }

    private fun loginUser() {
        val email = email_editText_sign_in.text.toString()
        val password = password_editText2_sign_in.text.toString()


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            if (it.isSuccessful) {
                Toast.makeText(this, "sign in successfully ${it.result.user}", Toast.LENGTH_LONG).show()

            } else {
                // it.result.user
                Toast.makeText(this, "else part ${it.result}", Toast.LENGTH_LONG).show()

            }

        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrog!! ${it.message.toString()}", Toast.LENGTH_LONG).show()

        }
    }
}
