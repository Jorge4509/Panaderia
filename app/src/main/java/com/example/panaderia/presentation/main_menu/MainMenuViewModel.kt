package com.example.panaderia.presentation.main_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.model.Sale
import com.example.panaderia.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class MainMenuState(
    val totalProfit: Double = 0.0,
    val totalLoss: Double = 0.0,
    val topSellingProduct: String = "N/A",
    val topSellingQuantity: Int = 0,
    val isLoading: Boolean = false
)

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val state: StateFlow<MainMenuState> = productRepository.getSales()
        .map { sales ->
            val totalProfit = sales.sumOf { it.total }
            // Simulación de pérdidas (por ejemplo, 5% de las ventas o basado en productos no vendidos/caducados si tuviéramos esa data)
            val totalLoss = totalProfit * 0.05 

            // Calcular el producto más vendido
            val productCounts = mutableMapOf<String, Int>()
            sales.forEach { sale ->
                sale.products.forEach { cartItem ->
                    val name = cartItem.product.name
                    productCounts[name] = productCounts.getOrDefault(name, 0) + cartItem.quantity
                }
            }
            val topProduct = productCounts.maxByOrNull { it.value }

            MainMenuState(
                totalProfit = totalProfit,
                totalLoss = totalLoss,
                topSellingProduct = topProduct?.key ?: "Ninguno",
                topSellingQuantity = topProduct?.value ?: 0
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainMenuState()
        )
}
