package com.example.panaderia.domain.repository

import com.example.panaderia.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ICustomerProductRepository {
    fun getPublishedProducts(): Flow<List<Product>>
    fun getPublishedProductsByCategory(category: String): Flow<List<Product>>
}
