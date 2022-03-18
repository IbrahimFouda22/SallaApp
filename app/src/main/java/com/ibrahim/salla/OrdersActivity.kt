package com.ibrahim.salla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ibrahim.salla.adapters.OrdersAdapter
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.OrdersModel

class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("orders")
        val arrayList = ArrayList<OrdersModel>()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerOrders)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        val valueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (data in snapshot.children)
                {
                    arrayList.add(data.getValue(OrdersModel::class.java)!!)
                }
                val adapter = OrdersAdapter(this@OrdersActivity,arrayList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Constants.toast(this@OrdersActivity,error.message)
            }
        }
        firebaseDatabase.addValueEventListener(valueEventListener)
    }
}