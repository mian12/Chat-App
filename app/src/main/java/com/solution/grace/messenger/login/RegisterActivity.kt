package com.solution.grace.messenger.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.solution.grace.messenger.R
import com.solution.grace.messenger.messages.LatestMesagesActivity
import com.solution.grace.messenger.model.User
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {


    val waitingDialog: SpotsDialog? = null

    var slectedPhotoURi: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        user_imageView_register.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 340)

        }
        register_button2.setOnClickListener {
            //            waitingDialog!=SpotsDialog(this)
//            waitingDialog!!.show()
//            waitingDialog!!.setMessage("Please wait..")
//            waitingDialog.setCancelable(false)
            registerUser()
        }

        sign_in_textView2.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 340 && resultCode == Activity.RESULT_OK && data != null) {
            slectedPhotoURi = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, slectedPhotoURi)

            user_imageView_register.setImageBitmap(bitmap)

        }
    }

    private fun registerUser() {
        val userName = userName_edittext_register.text.toString()
        val email = email_editText4_register.text.toString()
        val password = password_editText5_register.text.toString()
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener()
            {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else {
                    Toast.makeText(this, "User created successfully!!  : ${it.result.user.uid}", Toast.LENGTH_LONG).show()

                    uploadImageToFirebaseStorage()
                }
            }.addOnFailureListener() {

                Toast.makeText(this, "User Error is :  ${it.message.toString()}", Toast.LENGTH_LONG).show()

            }
        }

        //  waitingDialog!!.dismiss()

    }

    private fun uploadImageToFirebaseStorage() {

        val fileName = UUID.randomUUID().toString()

        var ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        if (slectedPhotoURi != null) {

            ref.putFile(slectedPhotoURi!!).addOnSuccessListener {

                ref.downloadUrl.addOnSuccessListener {

                    saveUserToDatabase(it.toString())
                }
            }
        }


    }

    private fun saveUserToDatabase(profileImageUrl: String) {

        val uid = FirebaseAuth.getInstance().uid.toString()

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, userName_edittext_register.text.toString(), profileImageUrl)

        ref.setValue(user)


        var intent = Intent(this, LatestMesagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)


    }


}
