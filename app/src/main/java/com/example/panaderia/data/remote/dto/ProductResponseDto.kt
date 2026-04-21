package com.example.panaderia.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: Double?,
    @SerializedName("imagenUrl") val imagenUrl: String?,
    @SerializedName("stock") val stock: Int?,
    @SerializedName("category") val category: String?,
    @SerializedName("published") val published: Boolean?,
    @SerializedName("peso") val peso: Double?,
    @SerializedName("unidad") val unidad: String?,
    @SerializedName("infoAdicional") val infoAdicional: String?
)
