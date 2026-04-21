package com.example.panaderia.domain.hardware

interface IAuthManager {
    fun authenticate(activity: Any, onResult: (Boolean) -> Unit)
}
