package com.salespro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.salespro.app.firebaseDatabase.FirebaseCommon
import com.salespro.app.model.CartProduct
import com.salespro.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    val productPrice = cartProducts.map {
        when(it){
            is Resource.Success -> {
                calculatePrice(it.data)
            }
            else -> null
        }
    }

    fun calculatePrice(data: List<CartProduct>?): Float {
        var price = 0.0
        data?.forEach {
            price += it.product.price * it.quantity
        }
        return price.toFloat()
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    init{
        getCartProducts()
    }

    private fun getCartProducts(){
        viewModelScope.launch { _cartProducts.emit((Resource.Loading())) }
        firestore.collection("users")
            .document(auth.uid!!)
            .collection("cart")
            .addSnapshotListener { value, error ->
                if(error != null){
                    viewModelScope.launch { _cartProducts.emit(Resource.Error(error.message!!.toString())) }
                    return@addSnapshotListener
                }
                cartProductDocuments = value!!.documents
                val cartProducts = value?.toObjects(CartProduct::class.java)
                viewModelScope.launch { _cartProducts.emit(Resource.Success(cartProducts!!)) }
            }
    }

    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){
        val index = _cartProducts.value.data?.indexOf(cartProduct)

        if (index != null && index != -1){
            val documentId = cartProductDocuments[index].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading())}
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if(cartProduct.quantity == 1){
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading())}
                    decreaseQuantity(documentId)
                }
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
            firebaseCommon.decreaseQuantity(documentId){result,exception ->
                if(exception != null){
                    viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message!!.toString())) }

                }

            }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){result,exception ->
            if(exception != null){
                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message!!.toString())) }
            }
        }
    }

    fun deleteProduct(cartProduct: CartProduct) {
        val index = _cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1){
            val documentId = cartProductDocuments[index].id
            viewModelScope.launch { _cartProducts.emit(Resource.Loading())}
            firestore.collection("users")
                .document(auth.uid!!)
                .collection("cart")
                .document(documentId)
                .delete()

        }
    }

}