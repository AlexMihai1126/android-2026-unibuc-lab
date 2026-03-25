package cst.unibucfmiif2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cst.unibucfmiif2026.ui.pages.HomePage
import cst.unibucfmiif2026.ui.pages.LoginPage
import cst.unibucfmiif2026.ui.pages.RegisterPage

@Composable
fun AuthNavigation(){
    val navController = rememberNavController()
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
                onRegisterClick = {
                    navController.navigate("homepage")
                }
            )
        }

        composable("homepage") {
            HomePage()
        }
    }
}