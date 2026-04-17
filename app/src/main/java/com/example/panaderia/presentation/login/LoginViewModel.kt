package com.example.panaderia.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _navigateToAdminAuth = MutableSharedFlow<Unit>()
    val navigateToAdminAuth: SharedFlow<Unit> = _navigateToAdminAuth.asSharedFlow()

    fun loginWithPassword() {
        // Lógica de validación de texto (Arquitectura Hexagonal: aquí llamarías a un caso de uso de Auth)
        if (username.isNotBlank() && password.isNotBlank()) {
            _isLoggedIn.value = true
        }
    }

    fun onAdminLoginClick() {
        _navigateToAdminAuth.tryEmit(Unit)
    }
}
