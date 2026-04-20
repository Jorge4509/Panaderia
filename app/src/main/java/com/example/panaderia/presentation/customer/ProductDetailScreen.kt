package com.example.panaderia.presentation.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    onNavigateBack: () -> Unit,
    onAddedToCart: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            )
        }
    ) { padding ->
        state.product?.let { product ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
            ) {
                // Imagen del Producto
                AsyncImage(
                    model = product.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    // Categoría
                    Surface(
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = product.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color(0xFFE67E22),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        repeat(4) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107)) }
                        Icon(Icons.Default.StarHalf, null, tint = Color(0xFFFFC107))
                        Text(" (4.5) 128 reseñas", color = Color.Gray, fontSize = 14.sp)
                    }

                    Text(
                        text = product.description,
                        color = Color.Gray,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Scale, null, modifier = Modifier.size(18.dp), tint = Color.Gray)
                        Text(" Peso: 500g", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.Timer, null, modifier = Modifier.size(18.dp), tint = Color.Gray)
                        Text(" Fresco hoy", fontSize = 14.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "$" + String.format(Locale.getDefault(), "%.2f", product.price),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD35400)
                    )
                    Text("En stock", color = Color(0xFF2ECC71), fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Selector de Cantidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Cantidad", fontWeight = FontWeight.Bold, color = Color.Gray)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            ) {
                                IconButton(onClick = { viewModel.decreaseQuantity() }) {
                                    Icon(Icons.Default.Remove, null)
                                }
                                Text(
                                    text = state.quantity.toString(),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(onClick = { viewModel.increaseQuantity() }) {
                                    Icon(Icons.Default.Add, null)
                                }
                            }
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Subtotal", color = Color.Gray)
                            Text(
                                "$" + String.format(Locale.getDefault(), "%.2f", state.subtotal),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            viewModel.onAddToCart()
                            onAddedToCart()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE67E22))
                    ) {
                        Icon(Icons.Default.ShoppingCart, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agregar al carrito", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text("Información adicional", fontWeight = FontWeight.Bold)
                    Text("• Hecho con ingredientes naturales", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                    Text("• Sin conservantes artificiales", color = Color.Gray)
                    Text("• Elaborado diariamente", color = Color.Gray)
                    Text("• Producto artesanal", color = Color.Gray)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
