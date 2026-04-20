package com.example.panaderia.presentation.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.hardware.IVibratorManager
import com.example.panaderia.domain.model.CartItem
import com.example.panaderia.domain.repository.ICartRepository
import com.example.panaderia.domain.use_case.CheckoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CartState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val total: Double = 0.0,
    val isEmpty: Boolean = true,
    val isOrderPlaced: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: ICartRepository,
    private val checkoutUseCase: CheckoutUseCase,
    private val vibratorManager: IVibratorManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _isOrderPlaced = MutableStateFlow(false)

    val state: StateFlow<CartState> = combine(
        cartRepository.cartItems,
        cartRepository.subtotal,
        _isLoading,
        _error,
        _isOrderPlaced
    ) { items, subtotal, isLoading, error, isOrderPlaced ->
        CartState(
            items = items,
            subtotal = subtotal,
            total = subtotal, // Envío gratis
            isEmpty = items.isEmpty(),
            isLoading = isLoading,
            error = error,
            isOrderPlaced = isOrderPlaced
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartState()
    )

    fun updateQuantity(productId: String, newQuantity: Int) {
        cartRepository.updateQuantity(productId, newQuantity)
    }

    fun removeItem(productId: String) {
        cartRepository.removeProduct(productId)
    }

    fun clearCart() {
        cartRepository.clearCart()
    }

    fun checkout() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            checkoutUseCase().fold(
                onSuccess = {
                    vibratorManager.vibrateSuccess()
                    _isOrderPlaced.value = true
                    _isLoading.value = false
                },
                onFailure = { throwable ->
                    _error.value = throwable.message ?: "Error al procesar la compra"
                    _isLoading.value = false
                }
            )
        }
    }

    fun resetOrderState() {
        _isOrderPlaced.value = false
        _error.value = null
    }
}
