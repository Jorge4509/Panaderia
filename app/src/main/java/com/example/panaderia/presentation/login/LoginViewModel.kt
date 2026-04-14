package com.example.panaderia.presentation.login

import androidx.lifecycle.ViewModel
import com.example.panaderia.domain.hardware.IAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: IAuthManager
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun onLoginClick() {
        authManager.authenticate { success ->
            _isLoggedIn.value = success
        }
    }
}
