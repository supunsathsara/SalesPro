package com.salespro.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Order(
    val orderStatus: String,
    val orderDate: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val orderId:Long = Random.nextLong(0, 1000000000000000000)+ totalPrice.toLong()
): Parcelable {
    constructor() : this("", "", 0.0f, emptyList())
}
