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
import androidx.compose.runtime.*
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
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onNavigateBack: () -> Unit,
    onEditProduct: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                placeholder = { Text("Buscar producto...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFEF9E7),
                    unfocusedContainerColor = Color(0xFFFEF9E7),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.products) { product ->
                    ProductItem(
                        product = product,
                        onEdit = { onEditProduct(product.id) },
                        onDelete = { viewModel.deleteProduct(product.id) },
                        onPublish = { viewModel.publishProduct(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(90.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF9E7)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3498DB).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontSize = 12.sp, lineHeight = 14.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFE67E22))
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPublish: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF9E7).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen Fija (50dp) con esquinas redondeadas
            AsyncImage(
                model = product.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Contenido de Texto con peso 1f para empujar botones
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Cant: ${product.quantity}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE67E22)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Botones de Acción compactos
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Publicar (Azul/Verde)
                ActionButton(
                    icon = Icons.Default.Share, 
                    color = if (product.isPublished) Color(0xFF2ECC71).copy(alpha = 0.5f) else Color(0xFF3498DB), 
                    onClick = onPublish,
                    enabled = !product.isPublished
                )
                ActionButton(icon = Icons.Default.Edit, color = Color(0xFFE67E22), onClick = onEdit)
                ActionButton(icon = Icons.Default.Delete, color = Color(0xFFE74C3C), onClick = onDelete)
            }
        }
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector, 
    color: Color, 
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    FilledIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(32.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color,
            contentColor = Color.White,
            disabledContainerColor = color.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}
