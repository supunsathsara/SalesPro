package com.salespro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.salespro.app.model.Product
import com.salespro.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val products: StateFlow<Resource<List<Product>>> = _products

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _products.emit(Resource.Loading())
        }
        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.toObjects(Product::class.java)
               viewModelScope.launch {
                     _products.emit(Resource.Success(products))
               }
            }
            .addOnFailureListener { exception ->
                viewModelScope.launch {
                    _products.emit(Resource.Error(exception.message.toString()))
                }
            }
    }
}