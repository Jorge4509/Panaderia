package com.example.panaderia.domain.model

import java.util.UUID

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String? = "Sin nombre",
    val quantity: Int? = 0,
    val price: Double? = 0.0,
    val category: String? = "Sin categoría",
    val description: String? = "Sin descripción",
    val isPublished: Boolean = false,
    val imageUri: String? = null
)
