package com.example.panaderia.data.hardware

import com.example.panaderia.domain.hardware.IAuthManager
import javax.inject.Inject

class AndroidAuthManager @Inject constructor() : IAuthManager {
    override fun authenticate(onResult: (Boolean) -> Unit) {
        // Simulación de huella digital para Galaxy A15
        onResult(true)
    }
}
