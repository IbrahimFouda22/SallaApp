package com.ibrahim.salla

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.ibrahim.salla.constants.Constants

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val toolBarReset = findViewById<Toolbar>(R.id.toolBarReset)
        val edtEmail = findViewById<TextInputEditText>(R.id.edtEmailReset)
        val edtEmailLayout = findViewById<TextInputLayout>(R.id.edtEmailLayoutReset)
        val btnSend = findViewById<Button>(R.id.btnSend)

        setSupportActionBar(toolBarReset)

        btnSend.setOnClickListener {
            if(!edtEmail.text.isNullOrEmpty())
            {
                edtEmailLayout.error=null
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Please Wait")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                val auth = FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(edtEmail.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        progressDialog.dismiss()
                        Constants.toast(this,"Mail is sent")
                    }
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Constants.toast(this,it.message.toString())
                }
            }
            else
            {
                if(edtEmail.text.isNullOrEmpty())
                    edtEmailLayout.error="Email Must Not Be Empty"
                else
                    edtEmailLayout.error=null
            }
        }

    }
}