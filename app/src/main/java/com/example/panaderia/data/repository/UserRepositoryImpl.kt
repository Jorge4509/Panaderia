package com.example.panaderia.data.repository

import com.example.panaderia.data.remote.PanaderiaApi
import com.example.panaderia.domain.model.UserEntity
import com.example.panaderia.domain.repository.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: PanaderiaApi
) : IUserRepository {
    
    override suspend fun registerUser(user: UserEntity): Boolean {
        return try {
            val response = api.register(user)
            response.getOrDefault(false)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun loginUser(user: UserEntity): Boolean {
        return try {
            val response = api.login(user)
            response.getOrDefault(false)
        } catch (e: Exception) {
            false
        }
    }
}
