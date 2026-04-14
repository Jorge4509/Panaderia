package com.example.panaderia.presentation.admin_auth

import androidx.lifecycle.ViewModel
import com.example.panaderia.domain.hardware.IAuthManager
import com.example.panaderia.domain.hardware.IVibratorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AdminAuthViewModel @Inject constructor(
    private val authManager: IAuthManager,
    private val vibratorManager: IVibratorManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun authenticateAdmin(onSuccess: () -> Unit) {
        _authState.value = AuthState.Loading
        authManager.authenticate { success ->
            if (success) {
                vibratorManager.vibrateSuccess()
                _authState.value = AuthState.Success
                onSuccess()
            } else {
                _authState.value = AuthState.Error("Autenticación fallida")
            }
        }
    }
}
