package com.example.panaderia.domain.model

import java.util.UUID

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Int,
    val price: Double,
    val category: String = "Artesanal",
    val description: String = "Pan artesanal recién horneado.",
    val isPublished: Boolean = false,
    val imageUri: String? = null
)
