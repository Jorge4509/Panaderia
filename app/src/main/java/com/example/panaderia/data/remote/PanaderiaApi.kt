package com.example.panaderia.data.remote

import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.model.Sale
import retrofit2.http.*

interface PanaderiaApi {

    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Product

    @POST("products")
    suspend fun saveProduct(@Body product: Product)

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: String, @Body product: Product)

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: String)

    @PATCH("products/{id}/publish")
    suspend fun publishProduct(@Path("id") id: String)

    @GET("sales")
    suspend fun getSales(): List<Sale>

    @POST("sales")
    suspend fun checkout(@Body sale: Sale)

    companion object {
        const val BASE_URL = "https://your-api-url.com/" // Replace with actual URL
    }
}
