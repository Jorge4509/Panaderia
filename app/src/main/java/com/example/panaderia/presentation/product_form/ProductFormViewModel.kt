package com.example.panaderia.presentation.product_form

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.hardware.ICameraManager
import com.example.panaderia.domain.hardware.IVibratorManager
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductFormState(
    val id: String? = null,
    val name: String = "",
    val quantity: String = "",
    val price: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class ProductFormViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val cameraManager: ICameraManager,
    private val vibratorManager: IVibratorManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductFormState())
    val state = _state.asStateFlow()

    init {
        val productId: String? = savedStateHandle["productId"]
        if (productId != null) {
            loadProduct(productId)
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch {
            val product = repository.getProductById(id)
            if (product != null) {
                _state.update { it.copy(
                    id = product.id,
                    name = product.name,
                    quantity = product.quantity.toString(),
                    price = product.price.toString(),
                    imageUri = product.imageUri?.let { Uri.parse(it) }
                ) }
            }
        }
    }

    fun onNameChange(newName: String) {
        _state.update { it.copy(name = newName) }
    }

    fun onQuantityChange(newQuantity: String) {
        _state.update { it.copy(quantity = newQuantity) }
    }

    fun onPriceChange(newPrice: String) {
        _state.update { it.copy(price = newPrice) }
    }

    fun getNewImageUri(): Uri {
        return cameraManager.getOutputUri()
    }

    fun onImageCaptured(uri: Uri?) {
        _state.update { it.copy(imageUri = uri) }
    }

    fun saveProduct() {
        val currentState = _state.value
        if (currentState.name.isBlank()) return

        // Ejecutar vibración antes de guardar
        vibratorManager.vibrateSuccess()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val product = Product(
                id = currentState.id ?: java.util.UUID.randomUUID().toString(),
                name = currentState.name,
                quantity = currentState.quantity.toIntOrNull() ?: 0,
                price = currentState.price.toDoubleOrNull() ?: 0.0,
                imageUri = currentState.imageUri?.toString()
            )
            
            val result = if (currentState.id == null) {
                repository.saveProduct(product)
            } else {
                repository.updateProduct(product)
            }

            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSaved = true) }
            }
        }
    }
}
