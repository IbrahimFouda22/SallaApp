package com.ibrahim.salla

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.ibrahim.salla.constants.Constants
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private lateinit var imgProfile : ShapeableImageView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolBar = findViewById<Toolbar>(R.id.toolBarProfile)
        setSupportActionBar(toolBar)

        imgProfile = findViewById(R.id.imgProfile)
        val imgBtnCamera : ImageButton = findViewById(R.id.imgBtnCamera)
        val txtName : TextView = findViewById(R.id.txtNameProfile)
        val edtName : EditText = findViewById(R.id.edtNameProfile)
        val edtPhone : EditText = findViewById(R.id.edtPhoneProfile)
        val imgBtnName : ImageButton = findViewById(R.id.imgBtnEditName)
        val imgBtnPhone : ImageButton = findViewById(R.id.imgBtnEditPhone)
        val btnSave : Button = findViewById(R.id.btnSaveProfile)

        if (Constants.imageProfile == null)
        {
            imgProfile.setImageResource(R.drawable.ic_profile)
            imgProfile.setStrokeColorResource(R.color.darker_grey)
        }
        else
        {
            Picasso.get().load(Constants.imageProfile).into(imgProfile)
            imgProfile.setStrokeColorResource(R.color.white)
        }

        if(!Constants.user.name.isNullOrEmpty())
        {
            txtName.text = Constants.user.name
            edtName.setText(Constants.user.name)
        }
        if(!Constants.user.name.isNullOrEmpty())
            edtPhone.setText(Constants.user.phone)

        imgBtnCamera.setOnClickListener{
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,1)
        }

        imgBtnName.setOnClickListener {
            edtName.isEnabled = true
            btnSave.visibility = View.VISIBLE
        }
        imgBtnPhone.setOnClickListener {
            edtPhone.isEnabled = true
            btnSave.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            val saveDialog = ProgressDialog(this)
            saveDialog.setMessage("Please Wait")
            saveDialog.setCanceledOnTouchOutside(false)
            saveDialog.show()
            val map = HashMap<String,Any>()
            if(edtName.isEnabled)
            {
                edtName.isEnabled = false
                map["name"]=edtName.text.toString()
            }
            if(edtPhone.isEnabled)
            {
                edtPhone.isEnabled = false
                map["phone"]=edtPhone.text.toString()
            }
            FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).updateChildren(map).addOnSuccessListener {
                saveDialog.dismiss()
                Constants.toast(this,"Done !")
                btnSave.visibility = View.GONE
                txtName.text = edtName.text.toString()
                Constants.user.name = edtName.text.toString()
                Constants.user.phone = edtPhone.text.toString()
                val sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE)
                val remember = sharedPreferences.getBoolean("Remember",false)
                if (remember)
                {
                    val editor = sharedPreferences.edit()
                    editor.putString("name", Constants.user.name)
                    editor.putString("phone", Constants.user.phone)
                    editor.apply()
                }
            }.addOnFailureListener {
                saveDialog.dismiss()
                Constants.toast(this, it.message!!)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
        if(requestCode == 1 && resultCode == RESULT_OK && data !=null )
        {
            FirebaseStorage.getInstance().reference.child(Constants.uId!!).putFile(data.data!!).addOnSuccessListener {
                progressDialog.dismiss()
                Constants.toast(this , "Uploaded Done !")
                Constants.imageProfile = data.data
                Picasso.get().load(Constants.imageProfile).into(imgProfile)
            }.addOnFailureListener{
                progressDialog.dismiss()
                Constants.toast(this, it.message!!)
            }
        }
        else
            progressDialog.dismiss()
    }
}