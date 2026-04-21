package com.example.panaderia.presentation.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit
) {
    val orangeColor = Color(0xFFFF8C00)
    val lightOrange = Color(0xFFFFF5E6)
    val yellowField = Color(0xFFFFE082)
    val context = LocalContext.current

    // Observar el estado de éxito del registro para mostrar el Toast
    LaunchedEffect(viewModel.registrationSuccess) {
        viewModel.registrationSuccess?.let { success ->
            if (success) {
                Toast.makeText(context, "¡Registro Exitoso! Bienvenido", Toast.LENGTH_LONG).show()
                // Opcionalmente navegar al login después de un éxito
                onNavigateToLogin()
            } else {
                Toast.makeText(context, "Error al registrar: El usuario podría ya existir", Toast.LENGTH_LONG).show()
            }
            // Resetear el estado para que no se repita el Toast al recomponer
            viewModel.registrationSuccess = null
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

            // Cuadro central de registro
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
                        placeholder = { Text("Nombre de usuario") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
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
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = yellowField,
                            unfocusedContainerColor = yellowField,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { viewModel.onRegisterClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (viewModel.isRegistering) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = "Registrarse",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Botón "¿Iniciar sesión?"
                    TextButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "¿Iniciar sesión?",
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
