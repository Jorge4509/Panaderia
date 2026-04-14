package com.example.panaderia.data.hardware

import com.example.panaderia.domain.hardware.IGpsManager
import javax.inject.Inject

class AndroidGpsManager @Inject constructor() : IGpsManager {
    override fun getLocation(): String = "-12.0463, -77.0427"
}
