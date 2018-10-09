package com.solution.grace.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title="Select user"



        fetchUser()
    }

    private fun fetchUser() {
        var adapter=GroupAdapter<ViewHolder>()


       val ref= FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                 //  val user= it.getValue(RegisterActivity.User::class.java)

                   // adapter.add(UserItem())
                }

              //  recyclerview_newMessage.adapter=adapter

            }



        })
    }

//    class UserItem:Item<ViewHolder>(){
//        override fun getLayout(): Int {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun bind(viewHolder: ViewHolder, position: Int) {
//
//        }
//
//
//
//    }
}
