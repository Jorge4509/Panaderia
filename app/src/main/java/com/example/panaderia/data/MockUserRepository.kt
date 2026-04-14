package com.example.panaderia.data

import com.example.panaderia.domain.IUserRepository
import com.example.panaderia.domain.UserEntity
import javax.inject.Inject

class MockUserRepository @Inject constructor() : IUserRepository {
    override suspend fun registerUser(user: UserEntity): Boolean {
        // Simulación de registro exitoso
        return true
    }
}
