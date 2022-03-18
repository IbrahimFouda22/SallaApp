package com.ibrahim.salla.models

class CartModel (){
     var catId :Int? = null
     var proId :String? = null
     var proImage :String? = null
     var proName :String? = null
     var proPrice :String? = null
     var quantity :Int? = null
     var totalPrice :String? = null

    constructor(catId:Int?,proId:String?,proImage:String?,proName:String?,proPrice:String?,quantity:Int?,totalPrice:String?):this()
    {
        this.catId=catId
        this.proId=proId
        this.proImage=proImage
        this.proName=proName
        this.proPrice=proPrice
        this.quantity=quantity
        this.totalPrice=totalPrice
    }

}