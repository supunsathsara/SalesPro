package com.salespro.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.salespro.app.model.User
import com.salespro.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        runBlocking {
            _register.emit(Resource.Loading())
        }

        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener {
                it.user?.let {
                    saveUserInformation(it.uid, user)

                }
            }.addOnFailureListener {

            }
    }

    private fun saveUserInformation(userUID: String,user: User) {
        // Save user information to Firestore
        db.collection("users").document(userUID).set(user)
            .addOnSuccessListener {
                // User information saved successfully
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                // User information failed to save
                _register.value = Resource.Error(it.message.toString())
            }

    }

}