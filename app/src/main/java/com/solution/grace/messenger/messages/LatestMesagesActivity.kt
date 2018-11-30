package com.solution.grace.messenger.messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.solution.grace.messenger.R
import com.solution.grace.messenger.login.RegisterActivity

class LatestMesagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_mesages)

        verifiyUserLoggined()
    }

    private fun verifiyUserLoggined() {

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            var intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        when (item?.itemId) {
            R.id.nev_newMessage -> {
                var intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.nev_signOut -> {

                FirebaseAuth.getInstance().signOut()

                var intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        }


        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.nev_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }
}
