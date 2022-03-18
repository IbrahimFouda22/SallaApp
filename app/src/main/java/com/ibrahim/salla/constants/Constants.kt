package com.ibrahim.salla.constants

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ibrahim.salla.models.ProductModel
import com.ibrahim.salla.models.UserModel

object Constants {
    var uId:String? =null
    var user : UserModel = UserModel()
    var arrayHome  = ArrayList<ProductModel>()
    var arrayMen  = ArrayList<ProductModel>()
    var arrayWomen  = ArrayList<ProductModel>()
    var arrayKids  = ArrayList<ProductModel>()
    var arrayFavorites  = ArrayList<ProductModel>()
    var arrayFavId = ArrayList<String?>()
    var imageProfile : Uri? = null
    fun toast(context:Context,massage:String)
    {
        Toast.makeText(context,massage,Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun internetConnection(context: Context):Boolean
    {
        val connectivityManager  = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo  = connectivityManager.activeNetworkInfo
        return !(networkInfo == null || !networkInfo.isConnected || !networkInfo.isAvailable)
    }
}