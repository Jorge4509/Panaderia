package com.example.panaderia.domain.use_case

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

        // Creamos la venta con los items actuales
        val subtotal = cartRepository.subtotal.value
        val sale = Sale(
            total = subtotal,
            products = cartItems
        )

        // Enviamos directamente al backend. El backend validará el stock.
        return productRepository.checkout(sale).onSuccess {
            cartRepository.clearCart()
        }
    }
}
