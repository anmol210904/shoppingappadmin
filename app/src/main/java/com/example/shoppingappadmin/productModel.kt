package com.example.shoppingappadmin

import android.os.Parcelable

class productModel() {
    var name: String = ""
    var catagory : String = ""
    var price: String = ""
    var disp: String = ""
    var imageURL: String = ""
    var images : ArrayList<String> = arrayListOf()
    // Secondary constructor
    constructor(name: String, price: String, disp: String, imageURL: String) : this() {
        this.name = name
        this.price = price
        this.disp = disp
        this.imageURL = imageURL
    }
}