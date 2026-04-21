package com.example.panaderia.data

import com.example.panaderia.domain.repository.IUserRepository
import com.example.panaderia.domain.model.UserEntity
import javax.inject.Inject

class MockUserRepository @Inject constructor() : IUserRepository {
    override suspend fun registerUser(user: UserEntity): Boolean {
        // Simulación de registro exitoso
        return true
    }

    override suspend fun loginUser(user: UserEntity): Boolean {
        // Simulación de login exitoso
        return true
    }
}
