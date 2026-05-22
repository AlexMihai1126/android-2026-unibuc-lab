package cst.unibucfmiif2026.network.dto

data class PermissionsResponse(
    val message: String? = null,
    val user: FirebaseUserDto? = null
)

data class FirebaseUserDto(
    val uid: String? = null,
    val email: String? = null,
    val name: String? = null,
    val picture: String? = null
)