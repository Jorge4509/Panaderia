package com.example.panaderia.data.repository

import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor() : ProductRepository {
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())

    override fun getAllProducts(): Flow<List<Product>> = _products.asStateFlow()

    override suspend fun saveProduct(product: Product): Result<Unit> {
        val currentList = _products.value.toMutableList()
        currentList.add(product)
        _products.value = currentList
        return Result.success(Unit)
    }

    override suspend fun deleteProduct(id: String): Result<Unit> {
        val currentList = _products.value.toMutableList()
        currentList.removeIf { it.id == id }
        _products.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        val currentList = _products.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == product.id }
        if (index != -1) {
            currentList[index] = product
            _products.value = currentList
        }
        return Result.success(Unit)
    }

    override suspend fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }
}
