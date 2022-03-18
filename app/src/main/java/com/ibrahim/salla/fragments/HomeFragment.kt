package com.ibrahim.salla.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ibrahim.salla.R
import com.ibrahim.salla.adapters.DefaultGridAdapter
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.ProductModel

class HomeFragment : Fragment() {

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerHome = view.findViewById<RecyclerView>(R.id.recyclerHome)
        //val arrayList = ArrayList<ProductModel>()
        recyclerHome.setHasFixedSize(true)
        recyclerHome.layoutManager = GridLayoutManager(context,2)

        Constants.arrayHome.clear()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Products")
        val firebaseFav = FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("favorites")

        val favValueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Constants.arrayFavorites.clear()
                Constants.arrayFavId.clear()
                for (data in snapshot.children)
                {
                    val favModel = data.getValue(ProductModel::class.java)
                    Constants.arrayFavorites.add(favModel!!)
                    Constants.arrayFavId.add(favModel.proId)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Constants.toast(context!!,error.message)
            }
        }

        firebaseFav.addValueEventListener(favValueEventListener)

        val valueEventListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children)
                {
                    val productModel = data.getValue(ProductModel::class.java)
                    //arrayList.add(productModel!!)
                    Constants.arrayHome.add(productModel!!)
                }
                val adapter = DefaultGridAdapter(context!!, Constants.arrayHome)
                recyclerHome.adapter=adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Constants.toast(context!!,error.message)
            }
        }
        firebaseDatabase.addValueEventListener(valueEventListener)
        return view
    }

}