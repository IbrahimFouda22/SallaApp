package com.ibrahim.salla

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ibrahim.salla.adapters.FavoriteAdapter
import com.ibrahim.salla.constants.Constants

class FavoriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        val toolBar = findViewById<Toolbar>(R.id.toolBarFavorite)
        setSupportActionBar(toolBar)

        //Constants.toast(this,Constants.arrayFavorites.size.toString())
        val recyclerView : RecyclerView = findViewById(R.id.recyclerFavorite)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = FavoriteAdapter(this,Constants.arrayFavorites)
    }
}