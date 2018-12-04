package com.solution.grace.messenger.views

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.solution.grace.messenger.R
import com.solution.grace.messenger.model.ChatMessageModel
import com.solution.grace.messenger.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.row_latest_message.view.*

class LatestMessageRow(val chatMessageModel: ChatMessageModel) : Item<ViewHolder>() {

        var chatPartnerUser:User?=null


    override fun getLayout(): Int {
        return R.layout.row_latest_message
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.latest_textView_message.text = chatMessageModel.text

        var chatPartnerId: String
        if (chatMessageModel.fromId == FirebaseAuth.getInstance().uid)
            chatPartnerId = chatMessageModel.toId
        else
            chatPartnerId = chatMessageModel.fromId


        val ref= FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser= p0.getValue(User::class.java)
                viewHolder.itemView.latest_textView_name.text=chatPartnerUser?.userName
                val targetImage=viewHolder.itemView.latest_imageView_user

                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImage)

            }
            override fun onCancelled(p0: DatabaseError) {
            }



        })
    }

}
