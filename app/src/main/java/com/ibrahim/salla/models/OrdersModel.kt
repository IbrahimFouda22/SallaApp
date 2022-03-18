package com.ibrahim.salla.models

class OrdersModel (){
    var totalItems : Int? = null
    var totalPrice : String? = null
    var key : String? = null

    constructor(totalItems:Int? , totalPrice : String? , key : String?) : this()
    {
        this.totalItems = totalItems
        this.totalPrice = totalPrice
        this.key = key
    }

    fun toMap () : HashMap<String,Any?>
    {
        val map = HashMap<String,Any?>()
        map["totalItems"] = this.totalItems
        map["totalPrice"] = this.totalPrice
        map["key"] = this.key
        return map
    }
}