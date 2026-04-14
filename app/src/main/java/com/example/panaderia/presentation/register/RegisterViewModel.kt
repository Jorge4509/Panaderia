package com.example.panaderia.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.IUserRepository
import com.example.panaderia.domain.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isRegistering by mutableStateOf(false)
    var registrationSuccess by mutableStateOf<Boolean?>(null)

    fun onRegisterClick() {
        viewModelScope.launch {
            isRegistering = true
            val success = userRepository.registerUser(UserEntity(username, password))
            registrationSuccess = success
            isRegistering = false
        }
    }
}
