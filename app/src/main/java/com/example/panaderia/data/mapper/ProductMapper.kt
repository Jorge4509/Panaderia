package com.example.panaderia.data.mapper

import com.example.panaderia.domain.model.Product
import java.util.UUID

/**
 * En una arquitectura hexagonal real, tendríamos objetos de transferencia de datos (DTOs)
 * para la base de datos (Entity) o para la red (Response). 
 * Este mapper se encargaría de convertir esos DTOs en modelos de Dominio.
 */

// Simulación de un DTO de Base de Datos
data class ProductEntity(
    val id: String,
    val name: String,
    val quantity: Int,
    val price: Double,
    val category: String,
    val description: String,
    val isPublished: Boolean,
    val imageUri: String?
)

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        price = this.price,
        category = this.category,
        description = this.description,
        isPublished = this.isPublished,
        imageUri = this.imageUri
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        price = this.price,
        category = this.category,
        description = this.description,
        isPublished = this.isPublished,
        imageUri = this.imageUri
    )
}
