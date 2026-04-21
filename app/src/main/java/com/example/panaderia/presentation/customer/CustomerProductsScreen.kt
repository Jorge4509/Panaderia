package com.example.panaderia.presentation.customer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.panaderia.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProductsScreen(
    viewModel: CustomerProductsViewModel,
    onNavigateToCart: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val categories = listOf("Todos", "Artesanal", "Pan Dulce", "Integral", "Especial")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = Color(0xFFE67E22),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Panadería Artesanal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pan fresco todos los días",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE67E22)
                    )
                }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateToCart) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = Color.DarkGray
                        )
                        if (state.cartItemsCount > 0) {
                            Surface(
                                modifier = Modifier
                                    .size(18.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-8).dp),
                                shape = CircleShape,
                                color = Color(0xFFE67E22)
                            ) {
                                Text(
                                    text = state.cartItemsCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.wrapContentSize(Alignment.Center)
                                )
                            }
                        }
                    }
                }
                
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = Color.Red.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Pan Artesanal Recién\nHorneado",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                color = Color(0xFF2D3436),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Buscador
            TextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(12.dp)),
                placeholder = { Text("¿Qué pan se te antoja hoy?", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFE67E22)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFF9C4).copy(alpha = 0.3f),
                    unfocusedContainerColor = Color(0xFFFFF9C4).copy(alpha = 0.2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Chips de Categorías
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = state.selectedCategory == category
                    Surface(
                        modifier = Modifier.clickable { viewModel.onCategorySelected(category) },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFFE67E22) else Color(0xFFFFF3E0),
                        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFFFE0B2)) else null
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isSelected) Color.White else Color(0xFFD35400),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "Nuestros Productos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${state.products.size} disponibles",
                    fontSize = 14.sp,
                    color = Color(0xFFE67E22)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(state.products) { product ->
                    CustomerProductCard(
                        product = product,
                        onAddToCart = { 
                            if (product.quantity > 0) {
                                onNavigateToDetail(product.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerProductCard(
    product: Product,
    onAddToCart: () -> Unit
) {
    val isOutOfStock = product.quantity <= 0

    Card(
        modifier = Modifier.fillMaxWidth().clickable(enabled = !isOutOfStock) { onAddToCart() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOutOfStock) Color(0xFFF5F5F5) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isOutOfStock) 0.dp else 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFDEBD0))
            ) {
                AsyncImage(
                    model = product.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = if (isOutOfStock) 0.5f else 1.0f
                )
                if (isOutOfStock) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "AGOTADO",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isOutOfStock) Color.Gray else Color(0xFF2D3436),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!isOutOfStock) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                            Text(text = " 4.8", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isOutOfStock) Color.LightGray.copy(alpha = 0.2f) else Color(0xFFE67E22).copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = product.category,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = if (isOutOfStock) Color.Gray else Color(0xFFD35400),
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = product.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "$${String.format("%.2f", product.price)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isOutOfStock) Color.Gray else Color(0xFFD35400)
                        )
                        Text(
                            text = "Precio por unidad",
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }
                    
                    FilledIconButton(
                        onClick = onAddToCart,
                        enabled = !isOutOfStock,
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFE67E22),
                            disabledContainerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart, 
                            contentDescription = null, 
                            modifier = Modifier.size(20.dp), 
                            tint = if (isOutOfStock) Color.White.copy(alpha = 0.5f) else Color.White
                        )
                    }
                }
            }
        }
    }
}
