package com.example.panaderia.domain.model

import java.util.Date
import java.util.UUID

data class Sale(
    val id: String = UUID.randomUUID().toString(),
    val total: Double,
    val date: Date = Date(),
    val products: List<CartItem>
)
