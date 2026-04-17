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
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToAdminAuth: () -> Unit
) {
    val orangeColor = Color(0xFFFF8C00)
    val lightOrange = Color(0xFFFFF5E6)
    val yellowField = Color(0xFFFFE082)

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

            // Cuadro central de login
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
                    TextField(
                        value = viewModel.username,
                        onValueChange = { viewModel.username = it },
                        placeholder = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = yellowField,
                            unfocusedContainerColor = yellowField,
                            focusedIndicatorColor = Color.Transparent,
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
                            focusedContainerColor = yellowField,
                            unfocusedContainerColor = yellowField,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { viewModel.loginWithPassword() },
                        colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = "Acceder",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Texto clickable para Admin
                    Text(
                        text = "¿Iniciar sesión como admin?",
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clickable { onNavigateToAdminAuth() },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    // Botón "¿Desea Registrarse?"
                    TextButton(
                        onClick = onNavigateToRegister,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "¿Desea Registrarse?",
                            color = Color.Gray,
                            fontSize = 12.sp
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
