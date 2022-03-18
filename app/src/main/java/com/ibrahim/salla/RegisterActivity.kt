package com.ibrahim.salla

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.UserModel
import java.util.*

class RegisterActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolBarRegister = findViewById<Toolbar>(R.id.toolBarRegister)

        val edtNameLayout = findViewById<TextInputLayout>(R.id.edtNameLayoutRegister)
        val edtName = findViewById<TextInputEditText>(R.id.edtNameRegister)

        val edtEmailLayout = findViewById<TextInputLayout>(R.id.edtEmailLayoutRegister)
        val edtEmail = findViewById<TextInputEditText>(R.id.edtEmailRegister)

        val edtPasswordLayout = findViewById<TextInputLayout>(R.id.edtPasswordLayoutRegister)
        val edtPassword = findViewById<TextInputEditText>(R.id.edtPasswordRegister)

        val edtPhoneLayout = findViewById<TextInputLayout>(R.id.edtPhoneLayoutRegister)
        val edtPhone = findViewById<TextInputEditText>(R.id.edtPhoneRegister)

        val edtBirthDateLayout = findViewById<TextInputLayout>(R.id.edtBirthDateLayoutRegister)
        val edtBirthDate = findViewById<TextInputEditText>(R.id.edtBirthDateRegister)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val btnRegister = findViewById<Button>(R.id.btnReg)

        setSupportActionBar(toolBarRegister)

        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        edtBirthDate.setOnClickListener{
            val datePicker = DatePickerDialog(this,{ _, year, month, dayOfMonth ->
                edtBirthDate.setText("$dayOfMonth/$month/$year")
            },year,month,day)
            datePicker.show()
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Please Wait")
        btnRegister.setOnClickListener {
            if(!edtName.text.isNullOrEmpty() && !edtEmail.text.isNullOrEmpty() && !edtPassword.text.isNullOrEmpty() && !edtPhone.text.isNullOrEmpty()&&
                !edtBirthDate.text.isNullOrEmpty())
            {
                edtNameLayout.error=null
                edtEmailLayout.error=null
                edtPasswordLayout.error=null
                edtPhoneLayout.error=null
                edtBirthDateLayout.error=null

                progressDialog.show()

                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(edtEmail.text.toString(),edtPassword.text.toString()).addOnSuccessListener(this){
                    val id = it.user!!.uid
                    val firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users")
                    val userModel = UserModel(edtName.text.toString(),edtEmail.text.toString(),edtPhone.text.toString(),edtBirthDate.text.toString(),
                        spinner.selectedItem.toString())
                    firebaseDatabase.child(id).setValue(userModel.toMap("cart","orders","favorites")).addOnCompleteListener { task ->
                        if(task.isSuccessful)
                        {
                            Constants.toast(this,"Successful !")
                            progressDialog.dismiss()
                            edtName.setText("")
                            edtEmail.setText("")
                            edtPassword.setText("")
                            edtPhone.setText("")
                            edtBirthDate.setText("")
                        }
                    }
                }.addOnFailureListener { e ->
                        Constants.toast(this,e.message.toString())
                        progressDialog.dismiss()
                    }
//

            }
            else
            {
                if(edtName.text.isNullOrEmpty())
                    edtNameLayout.error="Name Must Not Be Empty"
                else
                    edtNameLayout.error=null

                if(edtEmail.text.isNullOrEmpty())
                    edtEmailLayout.error="Email Must Not Be Empty"
                else
                    edtEmailLayout.error=null

                if(edtPassword.text.isNullOrEmpty())
                    edtPasswordLayout.error="Password Must Not Be Empty"
                else
                    edtPasswordLayout.error=null

                if(edtPhone.text.isNullOrEmpty())
                    edtPhoneLayout.error="Phone Must Not Be Empty"
                else
                    edtPhoneLayout.error=null

                if(edtBirthDate.text.isNullOrEmpty())
                    edtBirthDateLayout.error="BirthDate Must Not Be Empty"
                else
                    edtBirthDateLayout.error=null
            }
        }

    }
}