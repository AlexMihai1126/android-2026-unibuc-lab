package cst.unibucfmiif2026.ui.navigation

const val AUTH_GRAPH_ROUTE = "auth_graph"
const val MAIN_GRAPH_ROUTE = "main_graph"

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
}
object MainRoutes {
    const val ADDRESSES_PAGE = "addresses_page"
    const val HOMEPAGE = "homepage"
    const val ADDRESSES_DETAILS = "address_details_page/{address_id}"
    const val ARG_ADDRESS_ID = "address_id"
    const val ADDRESS_DETAILS_PAGE = "address_details_page"

    fun getAddressDetails(id : Long) = "$ADDRESS_DETAILS_PAGE/$id"
}