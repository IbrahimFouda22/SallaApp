package com.ibrahim.salla

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.ibrahim.salla.adapters.SearchAdapter
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.ProductModel


class SearchActivity : AppCompatActivity() {
    lateinit var txtSearch : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolBar = findViewById<Toolbar>(R.id.toolBarSearch)
        setSupportActionBar(toolBar)

        txtSearch = findViewById(R.id.edtSearch)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerSearch)
        val imgBtnMic = findViewById<ImageButton>(R.id.imgBtnMic)
        val arrayList = ArrayList<ProductModel>()
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        txtSearch.addTextChangedListener{
            arrayList.clear()
            for(product in Constants.arrayHome)
            {
                if(product.proName!!.contains(it.toString()))
                    arrayList.add(product)
            }
            recyclerView.adapter = SearchAdapter(this,arrayList)
        }
        imgBtnMic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech To Text")
            startActivityForResult(intent, 2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 2 && data != null)
        {
            val arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            txtSearch.setText(arrayList!![0])
        }
    }
}