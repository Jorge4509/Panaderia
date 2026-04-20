package com.example.panaderia.domain.use_case

import com.example.panaderia.domain.model.CartItem
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.repository.ICartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    operator fun invoke(product: Product, quantity: Int) {
        val cartItem = CartItem(product, quantity)
        cartRepository.addProduct(cartItem)
    }
}
