package cst.unibucfmiif2026.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cst.unibucfmiif2026.data.AppDatabase
import cst.unibucfmiif2026.data.entities.AddressEntity
import cst.unibucfmiif2026.data.entities.UserEntity
import cst.unibucfmiif2026.network.RetrofitClient
import cst.unibucfmiif2026.network.dto.toEntity
import cst.unibucfmiif2026.utils.createImagePart
import cst.unibucfmiif2026.utils.toPlainTextRequestBody
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddressDetailsViewModel(application: Application, val addressId: Long) :
    AndroidViewModel(application) {
    private val userDao = AppDatabase.getInstance(application).userDao()
    private val addressDao = AppDatabase.getInstance(application).addressDao()

    val users = userDao.getByAddressId(addressId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addUser(firstname: String, lastname: String) {
        viewModelScope.launch {
            userDao.insert(
                listOf(
                    UserEntity(
                        firstName = firstname,
                        lastName = lastname,
                        addressId = addressId
                    )
                )
            )
        }
    }

    fun addUserToApi(firstname: String, lastname: String, email: String, avatar: Uri?) {
        viewModelScope.launch {
            try {
                val avatarPart = avatar?.createImagePart()
                val result = RetrofitClient.usersLocalApi.addUser(
                    firstname = firstname.toPlainTextRequestBody(),
                    lastname = lastname.toPlainTextRequestBody(),
                    email = email.toPlainTextRequestBody(),
                    avatar = avatarPart
                )
                if(result.isSuccessful) {
                    userDao.insert(
                        listOf(
                            UserEntity(
                                firstName = firstname,
                                lastName = lastname,
                                addressId = addressId
                            )
                        )
                    )
                }
            } catch (err: Exception) {
                Log.e("UploadUser", "${err.message}")
            }
        }
    }

    // TODO - split navigation file
    // TODO - create reusable composables from form inputs
    // TODO - improve loading page at app first startup

    fun loadUsers() {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.usersApi.getUsers(1)
                userDao.insert(result.data.map { user ->
                    user.toEntity(addressId)
                })
            } catch (err: Exception) {
                //TODO - add exception handler
            }

        }
    }

    companion object {
        fun factory(
            application: Application,
            addressId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddressDetailsViewModel(application, addressId) as T
            }
        }
    }
}