package cst.unibucfmiif2026.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cst.unibucfmiif2026.ui.pages.LoginPage
import cst.unibucfmiif2026.ui.pages.RegisterPage
import cst.unibucfmiif2026.viewmodel.AuthState
import cst.unibucfmiif2026.viewmodel.AuthViewModel

fun NavGraphBuilder.authNavigation(
    navController: NavController,
    authViewModel: AuthViewModel,
    authState: AuthState,
    onAuthSuccess : () -> Unit
) {

    navigation(
        startDestination = AuthRoutes.LOGIN,
        route = AUTH_GRAPH_ROUTE
        ) {
        composable(AuthRoutes.LOGIN) {
            LoginPage(
                onRegisterClick = {
                    navController.navigate(AuthRoutes.REGISTER)
                },
                onLoginClickFirebase = authViewModel::loginWithFirebase,
                onLoginClickApi = authViewModel::loginWithApi,
                onLoginSuccess = onAuthSuccess,
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }

        composable(AuthRoutes.REGISTER) {
            RegisterPage(
                onLoginClick = {
                    navController.popBackStack()
                },
                onRegisterClick = authViewModel::register,
                onRegisterSuccess = onAuthSuccess,
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }
    }
}