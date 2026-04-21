package com.example.panaderia.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.model.UserEntity
import com.example.panaderia.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _navigateToAdminAuth = MutableSharedFlow<Unit>()
    val navigateToAdminAuth: SharedFlow<Unit> = _navigateToAdminAuth.asSharedFlow()

    fun loginWithPassword() {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, completa todos los campos"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val success = userRepository.loginUser(UserEntity(username, password))
            if (success) {
                _isLoggedIn.value = true
            } else {
                errorMessage = "Usuario o contraseña incorrectos"
            }
            isLoading = false
        }
    }

    fun onAdminLoginClick() {
        _navigateToAdminAuth.tryEmit(Unit)
    }
}
