package com.solution.grace.messenger.messages

import android.content.Intent
import android.os.Binder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.solution.grace.messenger.R
import com.solution.grace.messenger.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    companion object {
        val  KEY_USER="user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"

        fetchUser()


    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.e("newMessage", it.toString())

                    val user = it.getValue(User::class.java)

                    if (user != null) {
                        if (user.uid==FirebaseAuth.getInstance().uid)
                        else
                        adapter.add(UserItem(user))
                    }

                }

                adapter.setOnItemClickListener { item, view ->
                    val userItem=item as UserItem

                    val intent=Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra(KEY_USER,userItem.user)


                    startActivity(intent)
                    finish()

                }
                recyclerview_newMessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

    }

    class UserItem(val user: User) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            viewHolder.itemView.name_user.text = user.userName

            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_profile)
        }

        override fun getLayout(): Int {
            return R.layout.row_new_message
        }


    }


}


