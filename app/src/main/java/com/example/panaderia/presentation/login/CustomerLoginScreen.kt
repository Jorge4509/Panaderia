package com.example.panaderia.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomerLoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToEmployeeLogin: () -> Unit
) {
    val orangeColor = Color(0xFFE67E22) // Naranja artesanal
    val lightBeige = Color(0xFFFEF9E7)
    val fieldColor = Color(0xFFFFF9C4).copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenido a\nPanadería Artesanal",
                fontSize = 32.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF2D3436),
                lineHeight = 38.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = lightBeige),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Área de Clientes",
                        fontWeight = FontWeight.Bold,
                        color = orangeColor,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    TextField(
                        value = viewModel.username,
                        onValueChange = { viewModel.username = it },
                        placeholder = { Text("Usuario o Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = orangeColor,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        placeholder = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = orangeColor,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { viewModel.loginWithPassword() },
                        colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Entrar a comprar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate aquí",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Opción para ir al login de empleados/admin
            Text(
                text = "Acceso para Personal",
                modifier = Modifier.clickable { onNavigateToEmployeeLogin() },
                style = MaterialTheme.typography.bodyMedium,
                color = orangeColor.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
