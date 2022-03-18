package com.ibrahim.salla

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.UserModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val edtEmail = findViewById<TextInputEditText>(R.id.edtEmailLogin)
        val edtEmailLayout = findViewById<TextInputLayout>(R.id.edtEmailLayoutLogin)
        val edtPassword = findViewById<TextInputEditText>(R.id.edtPasswordLogin)
        val edtPasswordLayout = findViewById<TextInputLayout>(R.id.edtPasswordLayoutLogin)
        val toolBarLogin = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBarLogin)
        val txtResetPass = findViewById<TextView>(R.id.txtResetPassword)
        val checkRemember = findViewById<CheckBox>(R.id.checkRemember)


        setSupportActionBar(toolBarLogin)

        btnLogin.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Please Wait")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            if(!edtEmail.text.isNullOrEmpty() && !edtPassword.text.isNullOrEmpty())
            {
                edtEmailLayout.error=null
                edtPasswordLayout.error=null

                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(edtEmail.text.toString(),edtPassword.text.toString()).addOnSuccessListener(this){
                    Constants.uId=it.user!!.uid
                    FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).get().addOnSuccessListener { data ->
                        val user = data.getValue(UserModel::class.java)
                        Constants.user = user!!
                        if(checkRemember.isChecked)
                        {
                            val sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("Remember",true)
                            editor.putString("uId",Constants.uId)
                            editor.putString("name", Constants.user.name)
                            editor.putString("phone", Constants.user.phone)
                            editor.apply()
                        }
                        progressDialog.dismiss()
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()
                    }.addOnFailureListener { e->
                        Constants.toast(this, e.message!!)
                    }
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Constants.toast(this,it.message.toString())
                }
            }
            else
            {
                if(edtEmail.text.isNullOrEmpty())
                    edtEmailLayout.error ="Email Must Not Be Empty"
                else
                    edtEmailLayout.error=null
                if(edtPassword.text.isNullOrEmpty())
                    edtPasswordLayout.error ="Password Must Not Be Empty"
                else
                    edtPasswordLayout.error=null
            }
        }

        btnRegister.setOnClickListener {
            val i = Intent(this,RegisterActivity::class.java)
            startActivity(i)
        }

        txtResetPass.setOnClickListener{
            startActivity(Intent(this,ResetPasswordActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE)
        val remember = sharedPreferences.getBoolean("Remember",false)
        if (remember)
        {
            Constants.uId = sharedPreferences.getString("uId",null)
            Constants.user.name = sharedPreferences.getString("name",null)
            Constants.user.phone = sharedPreferences.getString("phone",null)
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }
}