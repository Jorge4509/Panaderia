package com.example.panaderia.domain.use_case

import com.example.panaderia.domain.model.CartItem
import com.example.panaderia.domain.model.Sale
import com.example.panaderia.domain.repository.ICartRepository
import com.example.panaderia.domain.repository.ProductRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: ICartRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val cartItems = cartRepository.cartItems.value
        if (cartItems.isEmpty()) return Result.failure(Exception("El carrito está vacío"))

        // 1. Validación de Stock
        for (item in cartItems) {
            val product = productRepository.getProductById(item.product.id)
            if (product == null || product.quantity < item.quantity) {
                return Result.failure(Exception("Stock insuficiente para: ${item.product.name}"))
            }
        }

        // 2. Crear Registro de Venta
        val subtotal = cartRepository.subtotal.value
        val sale = Sale(
            total = subtotal,
            products = cartItems
        )

        // 3. Procesar en Repositorio (Resta stock y guarda venta)
        return productRepository.checkout(sale).onSuccess {
            cartRepository.clearCart()
        }
    }
}
