package com.example.panaderia.presentation.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.panaderia.domain.model.CartItem
import com.example.panaderia.domain.model.Product
import com.example.panaderia.domain.repository.ICartRepository
import com.example.panaderia.domain.repository.ICustomerProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerProductsState(
    val products: List<Product> = emptyList(),
    val selectedCategory: String = "Todos",
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val cartItemsCount: Int = 0
)

@HiltViewModel
class CustomerProductsViewModel @Inject constructor(
    private val repository: ICustomerProductRepository,
    private val cartRepository: ICartRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("Todos")
    private val _searchQuery = MutableStateFlow("")

    val state: StateFlow<CustomerProductsState> = combine(
        _selectedCategory.flatMapLatest { category ->
            repository.getPublishedProductsByCategory(category)
        },
        _selectedCategory,
        _searchQuery,
        cartRepository.totalItemsCount
    ) { products, category, query, cartCount ->
        val filteredProducts = if (query.isBlank()) {
            products
        } else {
            products.filter { it.name.contains(query, ignoreCase = true) }
        }

        CustomerProductsState(
            products = filteredProducts,
            selectedCategory = category,
            searchQuery = query,
            cartItemsCount = cartCount
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CustomerProductsState()
    )

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onAddToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addProduct(CartItem(product, 1))
        }
    }
}
