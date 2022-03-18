package com.ibrahim.salla.models

class ProductModel (){
    var catId :Int? = null
    var catName :String? = null
    var proId :String? = null
    var proImage :String? = null
    var proName :String? = null
    var proPrice :String? = null

    fun toMap():HashMap<String,Any?>
    {
        val map = HashMap<String,Any?>()
        map["catId"]=this.catId
        map["catName"]=this.catName
        map["proId"]=this.proId
        map["proImage"]=this.proImage
        map["proName"]=this.proName
        map["proPrice"]=this.proPrice
        return map
    }

}