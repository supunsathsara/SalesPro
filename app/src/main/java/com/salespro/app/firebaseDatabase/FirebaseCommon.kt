package com.salespro.app.firebaseDatabase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.salespro.app.model.CartProduct

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth:FirebaseAuth
) {
    private val cartCollection = firestore.collection("users")
        .document(auth.uid!!)
        .collection("cart")

    fun addProductToCart(cartProduct: CartProduct,onResult: (CartProduct?,Exception?)->Unit){
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct,null)
            }
            .addOnFailureListener {
                onResult(null,it)
            }
    }

    fun increaseQuantity(documentId: String,onResult: (String?,Exception?)->Unit){
       firestore.runTransaction { transition ->
          val documentRef = cartCollection.document(documentId)
           val document = transition.get(documentRef)
           val productObject = document.toObject(CartProduct::class.java)
              productObject?.let {
                val newQuantity = it.quantity + 1
                val newProductObject =it.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
              }?:run {
                throw Exception("Product not found")
              }
       }
           .addOnSuccessListener {
               onResult(documentId,null)
           }
           .addOnFailureListener {
               onResult(null,it)
           }
    }
}