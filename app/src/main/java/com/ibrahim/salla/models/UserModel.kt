package com.ibrahim.salla.models

class UserModel {
    var name : String? = null
    var email : String? = null
    var phone : String? = null
    var birthDate : String? = null
    var gender : String? = null

    constructor()
    {

    }
    constructor(name:String?,email:String?,phone:String?,birthDate:String?,gender:String?)
    {
        this.name=name
        this.email=email
        this.phone=phone
        this.birthDate=birthDate
        this.gender=gender
        this.gender=gender
    }

    fun toMap (cart:String?,orders:String?,favorites:String?):HashMap<String,String?>
    {
        val map = HashMap<String,String?>()
        map["name"] = this.name
        map["email"] = this.email
        map["phone"] = this.phone
        map["birthDate"] = this.birthDate
        map["gender"] = this.gender
        map["cart"] = cart
        map["orders"] = orders
        map["favorites"] = favorites
        return map
    }
}