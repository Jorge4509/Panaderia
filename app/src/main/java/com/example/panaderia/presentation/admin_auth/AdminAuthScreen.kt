package com.example.panaderia.presentation.admin_auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminAuthScreen(
    viewModel: AdminAuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateBackToLogin: () -> Unit
) {
    val state by viewModel.authState.collectAsState()
    val orangeColor = Color(0xFFFF8C00)
    val lightOrange = Color(0xFFFFF5E6)
    val context = LocalContext.current

    // Lógica de la Huella: Lanzar automáticamente al aparecer la pantalla
    LaunchedEffect(Unit) {
        viewModel.authenticateAdmin(context) {
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Franja superior naranja
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(orangeColor)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Panes tradicionales",
                fontSize = 40.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = lightOrange),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (state) {
                            is AuthState.Loading -> "Autenticando..."
                            is AuthState.Success -> "¡Listo!"
                            is AuthState.Error -> (state as AuthState.Error).message
                            else -> "Esperando huella digital..."
                        },
                        color = if (state is AuthState.Success) Color(0xFF2ECC71) else Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Icon(
                        imageVector = if (state is AuthState.Success) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = "Lock",
                        modifier = Modifier.size(100.dp),
                        tint = if (state is AuthState.Success) Color(0xFF2ECC71) else Color.Black
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón de Regreso: Ejecuta el callback que hace popBackStack
                    Button(
                        onClick = onNavigateBackToLogin,
                        colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Regresar al Login",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Franja inferior naranja
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(orangeColor)
        )
    }
}
