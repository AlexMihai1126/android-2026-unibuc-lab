package cst.unibucfmiif2026.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import cst.unibucfmiif2026.ui.pages.AddressDetailsPage
import cst.unibucfmiif2026.ui.pages.AddressesPage
import cst.unibucfmiif2026.ui.pages.HomePage

fun NavGraphBuilder.mainNavigation(
    navController: NavController,
    onLogout: () -> Unit = {}
) {
    navigation(
        startDestination = MainRoutes.HOMEPAGE,
        route = MAIN_GRAPH_ROUTE
    ) {
        composable(MainRoutes.HOMEPAGE) {
            HomePage(
                onLogout = onLogout,
                gotoAddresses = { navController.navigate(MainRoutes.ADDRESSES_PAGE) }
            )
        }

        composable(MainRoutes.ADDRESSES_PAGE) {
            AddressesPage { addressId ->
                navController.navigate(MainRoutes.getAddressDetails(addressId))
            }
        }

        composable(
            route = MainRoutes.ADDRESSES_DETAILS,
            arguments = listOf(navArgument(MainRoutes.ARG_ADDRESS_ID) {
                type = NavType.LongType
            })
        ) {
            val addressId = it.arguments?.getLong(MainRoutes.ARG_ADDRESS_ID) ?: return@composable
            AddressDetailsPage(id = addressId)
        }
    }
}