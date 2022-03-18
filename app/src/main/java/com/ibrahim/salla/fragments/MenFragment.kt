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

class MenFragment : Fragment() {


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_men, container, false)
        val recyclerMen = view.findViewById<RecyclerView>(R.id.recyclerMen)
        recyclerMen.setHasFixedSize(true)
        recyclerMen.layoutManager = GridLayoutManager(context,2)

        Constants.arrayMen.clear()
        for (product in Constants.arrayHome)
        {
            if(product.catId==0)
                Constants.arrayMen.add(product)
        }

        val adapter = DefaultGridAdapter(context!!,Constants.arrayMen)
        recyclerMen.adapter = adapter

        return view
    }


}