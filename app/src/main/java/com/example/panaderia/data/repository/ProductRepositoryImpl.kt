package com.example.panaderia.data.repository

import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.model.Sale
import com.example.panaderia.domain.repository.ICustomerProductRepository
import com.example.panaderia.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor() : ProductRepository, ICustomerProductRepository {
    
    // Simulación de base de datos en memoria para el ejemplo
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    private val productsFlow = _products.asStateFlow()

    override fun getAllProducts(): Flow<List<Product>> = productsFlow

    override suspend fun saveProduct(product: Product): Result<Unit> {
        _products.update { it + product }
        return Result.success(Unit)
    }

    override suspend fun deleteProduct(id: String): Result<Unit> {
        _products.update { list -> list.filter { it.id != id } }
        return Result.success(Unit)
    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        _products.update { list -> list.map { if (it.id == product.id) product else it } }
        return Result.success(Unit)
    }

    override suspend fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }

    override suspend fun publishProduct(id: String): Result<Unit> {
        _products.update { list -> 
            list.map { if (it.id == id) it.copy(isPublished = true) else it }
        }
        return Result.success(Unit)
    }

    override suspend fun checkout(sale: Sale): Result<Unit> {
        // Restar stock de los productos vendidos
        _products.update { currentProducts ->
            currentProducts.map { product ->
                val soldItem = sale.products.find { it.product.id == product.id }
                if (soldItem != null) {
                    product.copy(quantity = product.quantity - soldItem.quantity)
                } else {
                    product
                }
            }
        }
        
        // En una implementación real, aquí también se guardaría la venta en una base de datos o API.
        return Result.success(Unit)
    }

    // Implementación de ICustomerProductRepository (Read-only para clientes)
    override fun getPublishedProducts(): Flow<List<Product>> {
        return productsFlow.map { list -> list.filter { it.isPublished } }
    }

    override fun getPublishedProductsByCategory(category: String): Flow<List<Product>> {
        return productsFlow.map { list -> 
            list.filter { it.isPublished && (category == "Todos" || it.category == category) }
        }
    }
}
