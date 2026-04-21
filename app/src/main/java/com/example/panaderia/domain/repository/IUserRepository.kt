package com.example.panaderia.domain.repository

import com.example.panaderia.domain.model.UserEntity

interface IUserRepository {
    suspend fun registerUser(user: UserEntity): Boolean
    suspend fun loginUser(user: UserEntity): Boolean
}
