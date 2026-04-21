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
import com.example.panaderia.presentation.customer.CartScreen
import com.example.panaderia.presentation.customer.CartViewModel
import com.example.panaderia.presentation.customer.CustomerProductsScreen
import com.example.panaderia.presentation.customer.CustomerProductsViewModel
import com.example.panaderia.presentation.customer.ProductDetailScreen
import com.example.panaderia.presentation.customer.ProductDetailViewModel
import com.example.panaderia.presentation.login.CustomerLoginScreen
import com.example.panaderia.presentation.login.EmployeeLoginScreen
import com.example.panaderia.presentation.login.LoginViewModel
import com.example.panaderia.presentation.main_menu.MainMenuScreen
import com.example.panaderia.presentation.main_menu.MainMenuViewModel
import com.example.panaderia.presentation.product_form.ProductFormScreen
import com.example.panaderia.presentation.product_form.ProductFormViewModel
import com.example.panaderia.presentation.product_list.ProductListScreen
import com.example.panaderia.presentation.product_list.ProductListViewModel
import com.example.panaderia.presentation.register.EmployeeRegisterScreen
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
    
    NavHost(navController = navController, startDestination = "customer_login") {
        
        composable("customer_login") {
            val viewModel: LoginViewModel = hiltViewModel()
            val isLoggedIn by viewModel.isLoggedIn.collectAsState()

            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn) {
                    navController.navigate("customer_products") {
                        popUpTo("customer_login") { inclusive = true }
                    }
                }
            }

            CustomerLoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate("customer_register")
                },
                onNavigateToEmployeeLogin = {
                    navController.navigate("employee_login")
                }
            )
        }

        composable("employee_login") {
            val viewModel: LoginViewModel = hiltViewModel()
            val isLoggedIn by viewModel.isLoggedIn.collectAsState()

            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn) {
                    navController.navigate("main_menu/false") {
                        popUpTo("employee_login") { inclusive = true }
                    }
                }
            }

            EmployeeLoginScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAdminAuth = {
                    navController.navigate("admin_auth")
                },
                onNavigateToEmployeeRegister = {
                    navController.navigate("employee_register")
                }
            )
        }

        composable("admin_auth") {
            val viewModel: AdminAuthViewModel = hiltViewModel()
            AdminAuthScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate("main_menu/true") {
                        popUpTo("customer_login") { inclusive = true }
                    }
                },
                onNavigateBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("customer_register") {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("employee_register") {
            val viewModel: RegisterViewModel = hiltViewModel()
            EmployeeRegisterScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("customer_products") {
            val viewModel: CustomerProductsViewModel = hiltViewModel()
            CustomerProductsScreen(
                viewModel = viewModel,
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                onNavigateToDetail = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                onLogout = {
                    navController.navigate("customer_login") {
                        popUpTo("customer_products") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            val viewModel: ProductDetailViewModel = hiltViewModel()
            ProductDetailScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAddedToCart = {
                    navController.popBackStack()
                }
            )
        }

        composable("cart") {
            val viewModel: CartViewModel = hiltViewModel()
            CartScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCheckoutSuccess = {
                    navController.navigate("customer_products") {
                        popUpTo("customer_products") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "main_menu/{isAdmin}",
            arguments = listOf(navArgument("isAdmin") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isAdmin = backStackEntry.arguments?.getBoolean("isAdmin") ?: false
            val viewModel: MainMenuViewModel = hiltViewModel()
            MainMenuScreen(
                isAdmin = isAdmin,
                viewModel = viewModel,
                onNavigateToProductForm = {
                    navController.navigate("product_form")
                },
                onNavigateToList = {
                    navController.navigate("product_list")
                },
                onLogout = {
                    navController.navigate("customer_login") {
                        popUpTo("main_menu/{isAdmin}") { inclusive = true }
                    }
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
