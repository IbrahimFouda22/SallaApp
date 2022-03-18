package com.ibrahim.salla.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibrahim.salla.R
import com.ibrahim.salla.adapters.DefaultGridAdapter
import com.ibrahim.salla.constants.Constants

class WomenFragment : Fragment() {


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_women, container, false)
        val recyclerWomen = view.findViewById<RecyclerView>(R.id.recyclerWomen)
        recyclerWomen.setHasFixedSize(true)
        recyclerWomen.layoutManager = GridLayoutManager(context,2)

        Constants.arrayWomen.clear()
        for (product in Constants.arrayHome)
        {
            if(product.catId==1)
                Constants.arrayWomen.add(product)
        }

        val adapter = DefaultGridAdapter(context!!,Constants.arrayWomen)
        recyclerWomen.adapter = adapter

        return view
    }

}