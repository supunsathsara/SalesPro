package com.salespro.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val price: Float,
    val description: String? = null,
    val imageUrl: String? = null,
    val qty: Int
):Parcelable{
    constructor():this("0","",0.0f,"",null,0)
}
