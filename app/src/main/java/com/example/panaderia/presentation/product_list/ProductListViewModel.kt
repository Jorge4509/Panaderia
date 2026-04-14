package com.example.panaderia.presentation.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductListState(
    val products: List<Product> = emptyList(),
    val searchQuery: String = "",
    val totalProducts: Int = 0,
    val totalCost: Double = 0.0,
    val isLoading: Boolean = false
)

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val state: StateFlow<ProductListState> = combine(
        repository.getAllProducts(),
        _searchQuery
    ) { products, query ->
        val filteredProducts = if (query.isBlank()) {
            products
        } else {
            products.filter { it.name.contains(query, ignoreCase = true) }
        }

        ProductListState(
            products = filteredProducts,
            searchQuery = query,
            totalProducts = products.sumOf { it.quantity },
            totalCost = products.sumOf { it.price * it.quantity }
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductListState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            repository.deleteProduct(id)
        }
    }
}
