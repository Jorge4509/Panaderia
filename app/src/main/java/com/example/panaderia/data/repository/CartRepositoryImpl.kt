package com.example.panaderia.data.repository

import com.example.panaderia.domain.model.CartItem
import com.example.panaderia.domain.repository.ICartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor() : ICartRepository {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    override val totalItemsCount: StateFlow<Int> = _cartItems
        .map { items -> items.sumOf { it.quantity } }
        .stateIn(GlobalScope, SharingStarted.Eagerly, 0)

    override val subtotal: StateFlow<Double> = _cartItems
        .map { items -> items.sumOf { it.product.price * it.quantity } }
        .stateIn(GlobalScope, SharingStarted.Eagerly, 0.0)

    override fun addProduct(cartItem: CartItem) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == cartItem.product.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.product.id == cartItem.product.id) {
                        it.copy(quantity = it.quantity + cartItem.quantity)
                    } else it
                }
            } else {
                currentItems + cartItem
            }
        }
    }

    override fun removeProduct(productId: String) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.product.id != productId }
        }
    }

    override fun updateQuantity(productId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeProduct(productId)
            return
        }
        _cartItems.update { currentItems ->
            currentItems.map {
                if (it.product.id == productId) it.copy(quantity = newQuantity) else it
            }
        }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }
}
