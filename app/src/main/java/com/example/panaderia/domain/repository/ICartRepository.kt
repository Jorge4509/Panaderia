package com.example.panaderia.domain.repository

import com.example.panaderia.domain.model.CartItem
import kotlinx.coroutines.flow.StateFlow

interface ICartRepository {
    val cartItems: StateFlow<List<CartItem>>
    val totalItemsCount: StateFlow<Int>
    val subtotal: StateFlow<Double>
    fun addProduct(cartItem: CartItem)
    fun removeProduct(productId: String)
    fun updateQuantity(productId: String, newQuantity: Int)
    fun clearCart()
}
