package com.solution.grace.messenger.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.solution.grace.messenger.R
import com.solution.grace.messenger.messages.LatestMesagesActivity
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
        if (email.isEmpty() || password.isEmpty())
            return

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            if (it.isSuccessful) {
                var intent = Intent(this, LatestMesagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            } else {
                Toast.makeText(this, "else part ${it.exception}", Toast.LENGTH_LONG).show()

            }

        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrog!! ${it.message.toString()}", Toast.LENGTH_LONG).show()

        }
    }
}
