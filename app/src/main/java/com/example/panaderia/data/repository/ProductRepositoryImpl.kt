package com.example.panaderia.data.repository

import android.util.Log
import com.example.panaderia.data.remote.PanaderiaApi
import com.example.panaderia.data.remote.TokenManager
import com.example.panaderia.data.remote.dto.ProductRequestDto
import com.example.panaderia.data.remote.dto.ProductResponseDto
import com.example.panaderia.data.remote.dto.SaleItemDto
import com.example.panaderia.data.remote.dto.SaleRequestDto
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
    private val api: PanaderiaApi,
    private val tokenManager: TokenManager
) : ProductRepository, ICustomerProductRepository {

    private fun ProductResponseDto.toDomain(): Product {
        return Product(
            id = this.id,
            name = this.nombre ?: "Sin nombre",
            quantity = this.stock ?: 0,
            price = this.precio ?: 0.0,
            category = this.category ?: "Sin categoría",
            description = this.descripcion ?: "Sin descripción",
            isPublished = this.published ?: false,
            imageUri = this.imagenUrl
        )
    }

    override fun getAllProducts(): Flow<List<Product>> = flow {
        try {
            val response = api.getProducts()
            emit(response.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "getAllProducts error: ${e.message}", e)
            emit(emptyList())
        }
    }

    override fun getSales(): Flow<List<Sale>> = flow {
        try {
            emit(api.getSales())
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "getSales error: ${e.message}", e)
            emit(emptyList())
        }
    }

    override suspend fun saveProduct(product: Product): Result<Unit> {
        return try {
            val requestDto = ProductRequestDto(
                nombre = product.name ?: "Sin nombre",
                descripcion = product.description ?: "Sin descripción",
                precio = product.price ?: 0.0,
                imagenUrl = product.imageUri,
                stock = product.quantity ?: 0,
                category = product.category ?: "Artesanal",
                published = product.isPublished
            )
            api.saveProduct(requestDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "saveProduct error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            api.deleteProduct(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "deleteProduct error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            val requestDto = ProductRequestDto(
                nombre = product.name ?: "Sin nombre",
                descripcion = product.description ?: "Sin descripción",
                precio = product.price ?: 0.0,
                imagenUrl = product.imageUri,
                stock = product.quantity ?: 0,
                category = product.category ?: "Artesanal",
                published = product.isPublished
            )
            api.updateProduct(product.id, requestDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "updateProduct error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: String): Product? {
        return try {
            api.getProductById(id).toDomain()
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "getProductById error: ${id}: ${e.message}", e)
            null
        }
    }

    override suspend fun publishProduct(id: String): Result<Unit> {
        return try {
            api.publishProduct(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "publishProduct error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun checkout(sale: Sale): Result<Unit> {
        return try {
            val clienteId = tokenManager.getUserId() ?: "default_client"
            val request = SaleRequestDto(
                total = sale.total,
                items = sale.products.map { SaleItemDto(it.product.id, it.quantity) }
            )
            api.checkout(clienteId, request)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "checkout error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override fun getPublishedProducts(): Flow<List<Product>> = flow {
        try {
            val response = api.getPublicados()
            emit(response.map { it.toDomain() })
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "getPublishedProducts error: ${e.message}", e)
            emit(emptyList())
        }
    }

    override fun getPublishedProductsByCategory(category: String): Flow<List<Product>> = flow {
        try {
            val response = api.getPublicados()
            val products = response.map { it.toDomain() }.filter {
                category == "Todos" || it.category == category 
            }
            emit(products)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "getPublishedProductsByCategory error: ${e.message}", e)
            emit(emptyList())
        }
    }
}
