package com.example.panaderia.data.repository

import com.example.panaderia.data.remote.PanaderiaApi
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.model.Sale
import com.example.panaderia.domain.repository.ICustomerProductRepository
import com.example.panaderia.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: PanaderiaApi
) : ProductRepository, ICustomerProductRepository {

    override fun getAllProducts(): Flow<List<Product>> = flow {
        try {
            emit(api.getProducts())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getSales(): Flow<List<Sale>> = flow {
        try {
            emit(api.getSales())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun saveProduct(product: Product): Result<Unit> {
        return try {
            api.saveProduct(product)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            api.deleteProduct(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            api.updateProduct(product.id, product)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: String): Product? {
        return try {
            api.getProductById(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun publishProduct(id: String): Result<Unit> {
        return try {
            api.publishProduct(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkout(sale: Sale): Result<Unit> {
        return try {
            api.checkout(sale)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPublishedProducts(): Flow<List<Product>> = flow {
        try {
            val products = api.getProducts().filter { it.isPublished }
            emit(products)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getPublishedProductsByCategory(category: String): Flow<List<Product>> = flow {
        try {
            val products = api.getProducts().filter { 
                it.isPublished && (category == "Todos" || it.category == category) 
            }
            emit(products)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}
