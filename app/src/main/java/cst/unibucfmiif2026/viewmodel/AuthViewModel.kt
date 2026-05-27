package cst.unibucfmiif2026.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import cst.unibucfmiif2026.data.AuthDataStore
import cst.unibucfmiif2026.network.RetrofitClient
import cst.unibucfmiif2026.network.dto.LoginRequestDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class ApiAuthState {
    LOADING,
    LOGGED_IN,
    LOGGED_OUT
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()
    val isLoggedInFirebase: Boolean
        get() = auth.currentUser != null
    val apiAuthState = AuthDataStore.tokenFlow.map{ token ->
        when(token.isNullOrBlank()) {
            false -> ApiAuthState.LOGGED_IN
            else -> ApiAuthState.LOGGED_OUT
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ApiAuthState.LOADING
    )

    fun loginWithFirebase(email: String, password: String, onSuccess: () -> Unit) {
        _authState.value = AuthState(isLoading = true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState()
                onSuccess()
            }
            .addOnFailureListener { error ->
                _authState.value = AuthState(errorMessage = error.message)
            }
    }

    fun loginWithApi(email: String, password: String, onSuccess: () -> Unit) {
        _authState.value = AuthState(isLoading = true)
        viewModelScope.launch {
            try {
                val result = RetrofitClient.authApi.login(LoginRequestDTO(email, password))
                result.token?.let { token ->
                    _authState.value = AuthState()
                    AuthDataStore.saveToken(token)
                    onSuccess()
                } ?: run {
                    _authState.value = AuthState(errorMessage = "Invalid token received")
                }
            } catch (e: Exception) {
                _authState.value = AuthState(errorMessage = e.message)
            }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        _authState.value = AuthState(isLoading = true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState()
                onSuccess()
            }
            .addOnFailureListener { error ->
                _authState.value = AuthState(errorMessage = error.message)
            }
    }

    fun logout() {
        auth.signOut()
        viewModelScope.launch {
            AuthDataStore.clearToken()
        }
    }
}