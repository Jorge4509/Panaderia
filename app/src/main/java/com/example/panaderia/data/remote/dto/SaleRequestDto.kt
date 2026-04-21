package com.example.panaderia.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SaleRequestDto(
    @SerializedName("total") val total: Double,
    @SerializedName("items") val items: List<SaleItemDto>
)

data class SaleItemDto(
    @SerializedName("productId") val productId: String,
    @SerializedName("cantidad") val cantidad: Int
)
