package com.salespro.app.model

import kotlin.random.Random

data class Order(
    val orderStatus: String,
    val orderDate: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val orderId:Long = Random.nextLong(0, 1000000000000000000)+ totalPrice.toLong()
) {
    constructor() : this("", "", 0.0f, emptyList())
}
