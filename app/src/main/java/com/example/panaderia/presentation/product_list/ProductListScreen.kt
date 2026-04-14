package com.example.panaderia.presentation.product_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.panaderia.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onNavigateBack: () -> Unit,
    onEditProduct: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de productos", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE67E22)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de productos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Cuadros de resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryCard(
                    title = "Total de Productos",
                    value = state.totalProducts.toString(),
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Costo Total",
                    value = "${String.format("%.2f", state.totalCost)}$",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buscador
            TextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, Color(0xFFE67E22), RoundedCornerShape(24.dp)),
                placeholder = { Text("Buscar por cliente o producto...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFEF9E7),
                    unfocusedContainerColor = Color(0xFFFEF9E7),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera de tabla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF9E7))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Vista", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("nombre", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Cantidad", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Precio", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(80.dp)) // Espacio para botones
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE67E22))
            ) {
                items(state.products) { product ->
                    ProductItem(
                        product = product,
                        onEdit = { onEditProduct(product.id) },
                        onDelete = { viewModel.deleteProduct(product.id) }
                    )
                    HorizontalDivider(color = Color(0xFFE67E22).copy(alpha = 0.3f))
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF9E7)),
        border = BorderStroke(1.dp, Color(0xFF3498DB)), // Borde azul como en la imagen
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Miniatura
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray)
                .weight(1f)
        ) {
            if (product.imageUri != null) {
                AsyncImage(
                    model = product.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text(product.name, modifier = Modifier.weight(2f), fontSize = 12.sp)
        Text(String.format("%.2f", product.quantity.toDouble()), modifier = Modifier.weight(1.5f), fontSize = 12.sp)
        Text(String.format("%.2f", product.price), modifier = Modifier.weight(1.5f), fontSize = 12.sp)

        Row(
            modifier = Modifier.width(90.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ActionButton(icon = Icons.Default.Share, color = Color(0xFF2ECC71), onClick = {})
            ActionButton(icon = Icons.Default.Edit, color = Color(0xFFE67E22), onClick = onEdit)
            ActionButton(icon = Icons.Default.Delete, color = Color(0xFFE74C3C), onClick = onDelete)
        }
    }
}

@Composable
fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(26.dp)
            .background(color, RoundedCornerShape(4.dp))
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
    }
}

private fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)
