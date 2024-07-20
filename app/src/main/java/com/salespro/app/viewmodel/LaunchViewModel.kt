package com.salespro.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel  @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel(){

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object{
        const val NAVIGATE_TO_LOGIN = 1
        const val NAVIGATE_TO_HOME = 2
    }

    init {
        val user = auth.currentUser
        if (user == null){
            // Navigate to login
            viewModelScope.launch {
                _navigate.emit(NAVIGATE_TO_LOGIN)
            }
        }else{
            // Navigate to home
            viewModelScope.launch {
                _navigate.emit(NAVIGATE_TO_HOME)
            }
        }
    }
}