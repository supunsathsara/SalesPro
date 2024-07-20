package com.salespro.app.model

data class Order(
    val orderStatus: String,
    val orderDate: String,
    val totalPrice: Float,
    val products: List<CartProduct>
)
