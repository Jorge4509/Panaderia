package com.example.panaderia.domain

interface IUserRepository {
    suspend fun registerUser(user: UserEntity): Boolean
}
