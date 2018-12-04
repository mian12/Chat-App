package com.solution.grace.messenger.messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.solution.grace.messenger.R
import com.solution.grace.messenger.login.RegisterActivity
import com.solution.grace.messenger.model.ChatMessageModel
import com.solution.grace.messenger.model.User
import com.solution.grace.messenger.views.LatestMessageRow
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.abc_activity_chooser_view.view.*
import kotlinx.android.synthetic.main.activity_latest_mesages.*
import kotlinx.android.synthetic.main.row_latest_message.view.*

class LatestMesagesActivity : AppCompatActivity() {

    companion object {

        var currentUserLoggedIn: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_mesages)

        recyclerview_latestMessage.adapter = adapterLatestMessage
        recyclerview_latestMessage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        adapterLatestMessage.setOnItemClickListener { item, view ->

            val row=item as LatestMessageRow // safe casting

            val intent=Intent(this,ChatLogActivity::class.java)
            intent.putExtra(NewMessageActivity.KEY_USER,row.chatPartnerUser)
            startActivity(intent)
        }


        listenForLatestMessage()

        fetchCurrentUser()

        verifiyUserLoggined()
    }


    val adapterLatestMessage = GroupAdapter<ViewHolder>()

    val latestMessageHashMap = HashMap<String, ChatMessageModel>()
    private fun refreshRecyclerViewChatMessage() {

        adapterLatestMessage.clear()
        latestMessageHashMap.values.forEach({
            adapterLatestMessage.add(LatestMessageRow(it))

        })

    }




    private fun listenForLatestMessage() {

        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest_message/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessageModel::class.java) ?: return

                latestMessageHashMap[p0.key!!] = chatMessage

                refreshRecyclerViewChatMessage()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessageModel = p0.getValue(ChatMessageModel::class.java) ?: return

                latestMessageHashMap[p0.key!!] = chatMessageModel
                refreshRecyclerViewChatMessage()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

    }



    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/${uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUserLoggedIn = p0.getValue(User::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {
            }


        })
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
