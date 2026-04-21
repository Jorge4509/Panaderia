package com.example.panaderia.data.remote

import com.example.panaderia.data.remote.dto.*
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.model.Sale
import com.example.panaderia.domain.model.UserEntity
import retrofit2.Response
import retrofit2.http.*

interface PanaderiaApi {

    @POST("auth/login")
    suspend fun login(@Body user: UserEntity): Response<LoginResponseDto>

    @POST("auth/register")
    suspend fun register(@Body user: UserEntity): Response<UserDto>

    @POST("auth/admin-login")
    suspend fun adminLogin(@Body user: UserEntity): Response<LoginResponseDto>

    @GET("panes")
    suspend fun getProducts(): List<ProductResponseDto>

    @GET("panes/publicados")
    suspend fun getPublicados(): List<ProductResponseDto>

    @GET("panes/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductResponseDto

    @POST("panes")
    suspend fun saveProduct(@Body product: ProductRequestDto)

    @PUT("panes/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body product: ProductRequestDto)

    @DELETE("panes/{id}")
    suspend fun deleteProduct(@Path("id") id: String)

    @PATCH("panes/{id}/publish")
    suspend fun publishProduct(@Path("id") id: String)

    @GET("sales")
    suspend fun getSales(): List<Sale>

    @POST("pedidos/checkout/{clienteId}")
    suspend fun checkout(
        @Path("clienteId") clienteId: String,
        @Body sale: SaleRequestDto
    )

    companion object {
        const val BASE_URL = "http://35.153.159.34:8080/"
    }
}
