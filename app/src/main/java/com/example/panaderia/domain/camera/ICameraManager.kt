package com.example.panaderia.domain.camera

import android.net.Uri

interface ICameraManager {
    suspend fun takePhoto(): Uri?
}
