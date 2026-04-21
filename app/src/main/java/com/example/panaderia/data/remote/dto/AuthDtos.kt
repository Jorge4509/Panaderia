package com.example.panaderia.data.remote.dto

import com.example.panaderia.domain.model.UserEntity

data class LoginResponseDto(
    val token: String?,
    val user: UserDto?
)

data class UserDto(
    val id: String?,
    val username: String,
    val role: String?
)
