package com.solution.grace.messenger.messages

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.solution.grace.messenger.R
import com.solution.grace.messenger.messages.NewMessageActivity.Companion.KEY_USER
import com.solution.grace.messenger.model.ChatMessageModel
import com.solution.grace.messenger.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "chatLog"
    }

    val adapter = GroupAdapter<ViewHolder>()


    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)


        recyclerview_chatLog.adapter = adapter

        // val gson = Gson()
        //val user = gson.fromJson<User>(intent.getStringExtra(NewMessageActivity.KEY_USER), User::class.java)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.KEY_USER)

        supportActionBar?.title = toUser!!.userName

        button_send_chatlog.setOnClickListener {
            Log.d(TAG, "attempt to send mesage..")
            performSendMessage()
        }



        listenForMessage()
    }

    private fun performSendMessage() {

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.KEY_USER)
        val toId = user.uid
        val messag = editText_chatLog.text.toString()

        // fromID means cureent user logged in
        val refFrom = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val refToUser = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()



        if (fromId == null) return

        val chatMessageModel = ChatMessageModel(refFrom.key.toString(), fromId, messag, System.currentTimeMillis() / 1000, toId)

        refFrom.setValue(chatMessageModel)

        refToUser.setValue(chatMessageModel)

        editText_chatLog.text.clear()
        recyclerview_chatLog.scrollToPosition(adapter.itemCount-1)


    }

    private fun listenForMessage() {
        // user current logged id=fromID
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {


            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val chatMessage = p0.getValue(ChatMessageModel::class.java)
                // user current logged id=fromID
                if (FirebaseAuth.getInstance().uid == chatMessage!!.fromId) {
                    adapter.add(ChatFromItem(chatMessage.text))
                } else {

                    adapter.add(ChatToItem(chatMessage.text))

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }


            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    class ChatFromItem(val msg: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textView_from_row_message.text = msg
        }

        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }


    }

    class ChatToItem(val msg: String) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textView_to_row_message.text = msg
        }

        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }


    }
}
