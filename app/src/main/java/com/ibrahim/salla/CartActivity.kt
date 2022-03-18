package com.ibrahim.salla

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ibrahim.salla.adapters.CartAdapter
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.CartModel
import com.ibrahim.salla.models.OrdersModel

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val toolBar = findViewById<Toolbar>(R.id.toolBarCart)
        setSupportActionBar(toolBar)

        val firebaseDatabaseOrders = FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("orders")
        var totalItems = 0
        var totalPrice = "0$"
        var tempTotalPrice: Int
        val recyclerCart = findViewById<RecyclerView>(R.id.recyclerCart)
        val btnMakeOrder = findViewById<Button>(R.id.btnMakeOrder)

        btnMakeOrder.setOnClickListener {
            if(totalItems > 0)
            {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Do you want to make an order ?").setMessage("Total item = $totalItems \n\nTotal price = $totalPrice")
                    .setPositiveButton("OK"){
                            dialog,_ ->
                        dialog.dismiss()

                        val progressDialog = ProgressDialog(this)
                        progressDialog.setMessage("Please wait")
                        progressDialog.setCanceledOnTouchOutside(false)
                        progressDialog.show()

                        val key = firebaseDatabaseOrders.push().key
                        val map = OrdersModel(totalItems,totalPrice,key).toMap()
                        firebaseDatabaseOrders.child(key!!).updateChildren(map).addOnSuccessListener {
                            progressDialog.dismiss()
                            Constants.toast(this,"Done !")
                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            Constants.toast(this, it.message!!)
                        }
                    }.setNegativeButton("Cancel"){
                            dialog,_ ->
                        dialog.dismiss()
                    }.show()
            }
            else
                Constants.toast(this,"No items in your cart")
        }

        recyclerCart.setHasFixedSize(true)
        recyclerCart.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        val arrayList = ArrayList<CartModel>()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("cart")
        val valueEventListener = object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                totalItems = 0
                tempTotalPrice = 0
                for (data in snapshot.children)
                {
                    val item = data.getValue(CartModel::class.java)
                    arrayList.add(item!!)
                    totalItems += item.quantity!!
                    val arrayTotalPrice = item.totalPrice!!.split("$")
                    tempTotalPrice += arrayTotalPrice[0].toInt()
                }
                totalPrice = "$tempTotalPrice$"
                val adapter = CartAdapter(this@CartActivity,arrayList)
                recyclerCart.adapter=adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Constants.toast(this@CartActivity,error.message)
            }

        }
        firebaseDatabase.addValueEventListener(valueEventListener)
    }
}