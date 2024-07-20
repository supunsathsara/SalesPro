package com.salespro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.salespro.app.model.Order
import com.salespro.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {
    private val _order = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val order = _order.asStateFlow()

    fun placeOrder(order: Order){
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }

        firestore.runBatch {batch ->

            firestore.collection("users")
                .document(auth.uid!!)
                .collection("orders")
                .document()
                .set(order)

            firestore.collection("orders")
                .document()
                .set(order)

            firestore.collection("users")
                .document(auth.uid!!)
                .collection("cart")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }

            // Update the stock of the products
           /* firestore.collection("products")
                .get()
                .addOnSuccessListener {
                    it.documents.forEach { doc ->
                        order.products.forEach { cartProduct ->
                            if(doc.id == cartProduct.product.id){
                                val stock = doc.getLong("qty")!!.toInt() - cartProduct.quantity
                                batch.update(doc.reference, "qty", stock)
                            }
                        }
                    }
                }

            */

        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource.Success(order))
            }
        }.addOnFailureListener{
            viewModelScope.launch {
                _order.emit(Resource.Error(it.message.toString()))
            }
        }
    }

}