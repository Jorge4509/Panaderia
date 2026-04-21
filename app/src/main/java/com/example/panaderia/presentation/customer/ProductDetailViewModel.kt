package com.example.panaderia.presentation.customer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.hardware.IVibratorManager
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.repository.ICustomerProductRepository
import com.example.panaderia.domain.use_case.AddToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductDetailState(
    val product: Product? = null,
    val quantity: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null,
    val subtotal: Double = 0.0
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ICustomerProductRepository,
    private val addToCartUseCase: AddToCartUseCase,
    private val vibratorManager: IVibratorManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    init {
        val productId: String? = savedStateHandle["productId"]
        productId?.let { loadProduct(it) }
    }

    private fun loadProduct(id: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // Asumiendo que podemos obtener un producto por ID desde el repositorio de clientes
            // Si no existe, podríamos filtrar getPublishedProducts()
            repository.getPublishedProducts().collect { products ->
                val product = products.find { it.id == id }
                if (product != null) {
                    _state.update { it.copy(
                        product = product,
                        isLoading = false,
                        subtotal = product.price ?: 0.0
                    ) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "Producto no encontrado") }
                }
            }
        }
    }

    fun increaseQuantity() {
        _state.update { currentState ->
            val newQuantity = currentState.quantity + 1
            currentState.copy(
                quantity = newQuantity,
                subtotal = (currentState.product?.price ?: 0.0) * newQuantity
            )
        }
    }

    fun decreaseQuantity() {
        _state.update { currentState ->
            if (currentState.quantity > 1) {
                val newQuantity = currentState.quantity - 1
                currentState.copy(
                    quantity = newQuantity,
                    subtotal = (currentState.product?.price ?: 0.0) * newQuantity
                )
            } else currentState
        }
    }

    fun onAddToCart() {
        val product = _state.value.product ?: return
        val quantity = _state.value.quantity
        
        addToCartUseCase(product, quantity)
        vibratorManager.vibrateSuccess() // Vibración de confirmación
    }
}
