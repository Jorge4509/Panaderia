package com.example.panaderia

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.panaderia.presentation.admin_auth.AdminAuthScreen
import com.example.panaderia.presentation.admin_auth.AdminAuthViewModel
import com.example.panaderia.presentation.login.LoginScreen
import com.example.panaderia.presentation.login.LoginViewModel
import com.example.panaderia.presentation.main_menu.MainMenuScreen
import com.example.panaderia.presentation.product_form.ProductFormScreen
import com.example.panaderia.presentation.product_form.ProductFormViewModel
import com.example.panaderia.presentation.product_list.ProductListScreen
import com.example.panaderia.presentation.product_list.ProductListViewModel
import com.example.panaderia.presentation.register.RegisterScreen
import com.example.panaderia.presentation.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainNavigation()
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val viewModel: LoginViewModel = hiltViewModel()
            val isLoggedIn by viewModel.isLoggedIn.collectAsState()

            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn) {
                    navController.navigate("main_menu") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToAdminAuth = {
                    navController.navigate("admin_auth")
                }
            )
        }
        composable("register") {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable("admin_auth") {
            val viewModel: AdminAuthViewModel = hiltViewModel()
            AdminAuthScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate("main_menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateBackToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main_menu") {
            MainMenuScreen(
                onNavigateToProductForm = {
                    navController.navigate("product_form")
                },
                onNavigateToList = {
                    navController.navigate("product_list")
                }
            )
        }
        composable(
            route = "product_form?productId={productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            val viewModel: ProductFormViewModel = hiltViewModel()
            ProductFormScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("product_list") {
            val viewModel: ProductListViewModel = hiltViewModel()
            ProductListScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEditProduct = { productId ->
                    navController.navigate("product_form?productId=$productId")
                }
            )
        }
    }
}
