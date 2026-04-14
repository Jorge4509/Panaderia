package com.example.panaderia.domain.hardware

interface IAuthManager {
    fun authenticate(onResult: (Boolean) -> Unit)
}