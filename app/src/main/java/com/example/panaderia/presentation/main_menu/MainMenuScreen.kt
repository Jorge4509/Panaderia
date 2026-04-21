package com.example.panaderia.presentation.main_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun MainMenuScreen(
    isAdmin: Boolean = false,
    viewModel: MainMenuViewModel,
    onNavigateToProductForm: () -> Unit,
    onNavigateToList: () -> Unit,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas salir del sistema?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
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
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Panadería Artesanal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D1B2A)
                    )
                    Text(
                        text = if (isAdmin) "Panel de Administrador" else "Panel de Empleado",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE67E22),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            IconButton(onClick = { showLogoutDialog = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECCIÓN EXCLUSIVA PARA EL ADMIN (Dinámica)
        if (isAdmin) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Reporte en Tiempo Real",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436)
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Ganancia Total",
                        value = "$" + String.format(Locale.getDefault(), "%.2f", state.totalProfit),
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        color = Color(0xFF2ECC71),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Pérdidas (Mermas)",
                        value = "$" + String.format(Locale.getDefault(), "%.2f", state.totalLoss),
                        icon = Icons.AutoMirrored.Filled.TrendingDown,
                        color = Color(0xFFE74C3C),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Pan más vendido dinámico
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF1C40F).copy(alpha = 0.2f)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFF1C40F),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Producto Estrella", fontSize = 12.sp, color = Color.Gray)
                            Text(state.topSellingProduct, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text("${state.topSellingQuantity} vendidos", fontWeight = FontWeight.Bold, color = Color(0xFFE67E22))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SECCIÓN DE GESTIÓN
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Gestión de Inventario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3436)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MenuCard(
                    title = "Agregar",
                    subtitle = "Productos",
                    icon = Icons.Default.AddCircle,
                    containerColor = Color(0xFFE67E22),
                    contentColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToProductForm
                )
                MenuCard(
                    title = "Inventario",
                    subtitle = "Completo",
                    icon = Icons.Default.List,
                    containerColor = Color.White,
                    contentColor = Color(0xFFE67E22),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToList,
                    showBorder = true
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showBorder: Boolean = false
) {
    Card(
        modifier = modifier
            .aspectRatio(1.2f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = if (showBorder) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE67E22).copy(alpha = 0.5f)) else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor
            )
        }
    }
}
