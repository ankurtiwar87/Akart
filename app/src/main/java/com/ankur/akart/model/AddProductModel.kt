package com.ankur.akart.model


data class AddProductModel(

    val productName:String? ="",
    val productDescription:String? ="",
    val productCoverImg:String? ="",
    val productCategory:String? ="",
    val productId:String? ="",
    val productMRP:String? ="",
    val productSP:String? ="",
    val productImage:ArrayList<String> = ArrayList()


)
