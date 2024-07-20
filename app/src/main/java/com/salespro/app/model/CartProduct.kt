package com.salespro.app.model

data class CartProduct(
    val product: Product,
    val quantity: Int,
){
    constructor():this(Product(),1)
}
