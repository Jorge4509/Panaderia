package com.example.panaderia.data.repository

import android.util.Log
import com.example.panaderia.data.remote.PanaderiaApi
import com.example.panaderia.data.remote.TokenManager
import com.example.panaderia.domain.model.UserEntity
import com.example.panaderia.domain.repository.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: PanaderiaApi,
    private val tokenManager: TokenManager
) : IUserRepository {
    
    override suspend fun registerUser(user: UserEntity): Boolean {
        return try {
            Log.d("API_DEBUG", "Intentando registrar usuario: ${user.username}")
            val response = api.register(user)
            
            if (response.isSuccessful) {
                val userDto = response.body()
                Log.d("API_DEBUG", "Register success: User ID ${userDto?.id}")
                true
            } else {
                val errorMsg = response.errorBody()?.string()
                Log.e("API_DEBUG", "Register failed: ${response.code()} - $errorMsg")
                false
            }
        } catch (e: Exception) {
            Log.e("API_DEBUG", "Register exception: ${e.message}", e)
            false
        }
    }

    override suspend fun loginUser(user: UserEntity): Boolean {
        return try {
            Log.d("API_DEBUG", "Intentando login para: ${user.username}")
            val response = api.login(user)
            
            if (response.isSuccessful) {
                val loginResponse = response.body()
                loginResponse?.token?.let { tokenManager.saveToken(it) }
                loginResponse?.user?.id?.let { tokenManager.saveUserId(it) }
                Log.d("API_DEBUG", "Login success: Token y UserID guardados")
                true
            } else {
                val errorMsg = response.errorBody()?.string()
                Log.e("API_DEBUG", "Login failed: ${response.code()} - $errorMsg")
                false
            }
        } catch (e: Exception) {
            Log.e("API_DEBUG", "Login exception: ${e.message}", e)
            false
        }
    }

    suspend fun adminLogin(user: UserEntity): Boolean {
        return try {
            val response = api.adminLogin(user)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                loginResponse?.token?.let { tokenManager.saveToken(it) }
                loginResponse?.user?.id?.let { tokenManager.saveUserId(it) }
                Log.d("API_DEBUG", "Admin Login success: Token y UserID guardados")
                true
            } else {
                Log.e("API_DEBUG", "Admin Login failed: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("API_DEBUG", "Admin Login exception: ${e.message}", e)
            false
        }
    }
}
