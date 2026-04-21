package com.example.panaderia.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeLoginScreen(
    viewModel: LoginViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAdminAuth: () -> Unit,
    onNavigateToEmployeeRegister: () -> Unit
) {
    val darkBlue = Color(0xFF2C3E50)
    val lightBlue = Color(0xFFEBF5FB)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acceso de Personal", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBlue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = lightBlue)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Acceso Empleado",
                        fontSize = 20.sp,
                        color = darkBlue,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    TextField(
                        value = viewModel.username,
                        onValueChange = { viewModel.username = it },
                        label = { Text("Usuario de Empleado") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { viewModel.loginWithPassword() },
                        colors = ButtonDefaults.buttonColors(containerColor = darkBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Iniciar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onNavigateToEmployeeRegister) {
                        Text(
                            text = "¿Eres nuevo empleado? Regístrate aquí",
                            color = darkBlue.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ENLACE A ADMIN AUTH (HUELLA)
                    Text(
                        text = "¿Eres Administrador? Usa tu Huella",
                        modifier = Modifier.clickable { onNavigateToAdminAuth() },
                        color = Color(0xFFE67E22),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
