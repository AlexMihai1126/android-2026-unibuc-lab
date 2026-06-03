package cst.unibucfmiif2026.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cst.unibucfmiif2026.ui.components.AuthLoading
import cst.unibucfmiif2026.viewmodel.ApiAuthState
import cst.unibucfmiif2026.viewmodel.AuthViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    val apiAuthState by authViewModel.apiAuthState.collectAsState()

    val startDestination = when {
        authViewModel.isLoggedInFirebase -> MAIN_GRAPH_ROUTE
        apiAuthState == ApiAuthState.LOADING -> null
        apiAuthState == ApiAuthState.LOGGED_IN -> MAIN_GRAPH_ROUTE
        else -> AUTH_GRAPH_ROUTE
    }

    if (startDestination == null) {
        AuthLoading(modifier = modifier)
        return
    }

    NavHost(navController, startDestination = startDestination, modifier = modifier) {
        authNavigation(
            navController,
            authViewModel,
            authState,
            onAuthSuccess = {
                navController.navigate(MAIN_GRAPH_ROUTE) {
                    popUpTo(AUTH_GRAPH_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )
        mainNavigation(
            navController,
            onLogout = {
                authViewModel.logout()
                navController.navigate(AUTH_GRAPH_ROUTE) {
                    popUpTo(MAIN_GRAPH_ROUTE) {
                        inclusive = true
                    }
                }
            })

    }
}