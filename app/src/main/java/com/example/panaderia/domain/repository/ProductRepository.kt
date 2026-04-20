package com.example.panaderia.domain.repository

import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.model.Sale
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun saveProduct(product: Product): Result<Unit>
    suspend fun deleteProduct(id: String): Result<Unit>
    suspend fun updateProduct(product: Product): Result<Unit>
    suspend fun getProductById(id: String): Product?
    suspend fun publishProduct(id: String): Result<Unit>
    suspend fun checkout(sale: Sale): Result<Unit>
}
