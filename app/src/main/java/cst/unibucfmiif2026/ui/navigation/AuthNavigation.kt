package cst.unibucfmiif2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cst.unibucfmiif2026.ui.pages.HomePage
import cst.unibucfmiif2026.ui.pages.LoginPage
import cst.unibucfmiif2026.ui.pages.RegisterPage
import cst.unibucfmiif2026.viewmodel.AuthViewModel

@Composable
fun AuthNavigation(authViewModel: AuthViewModel = viewModel()){
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val navigateToHome : () -> Unit = { navController.navigate("homepage") }
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginPage(
                onRegisterClick = {
                    navController.navigate("register")
                },
                onLoginClick = {
                    navController.navigate("homepage")
                }
            )
        }

        composable("register") {
            RegisterPage(
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterClick = authViewModel::register,
                onRegisterSuccess = navigateToHome,
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }

        composable("homepage") {
            HomePage()
        }
    }
}