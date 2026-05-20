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

    fun addUserToApi(firstname: String, lastname: String, email: String, avatar: Uri) {
        viewModelScope.launch {
            try {
                // TODO - upload user
                // val result = RetrofitClient.usersLocalApi.addUser(firstname = firstname, lastname = lastname, email = email, avatar)
            } catch (e: Exception) {

            }
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

    // TODO - split navigation file
    // TODO - create reusable composables from form inputs
    // TODO - improve loading page at app first startup
// TODO - Uri to Part

//    private fun createImagePart(imageUri: Uri): MultipartBody.Part? {
//        val contentResolver = getApplication<Application>().contentResolver
//        val mimeType = contentResolver.getType(imageUri) ?: "image/jpeg"
//        val imageBytes = contentResolver.openInputStream(imageUri)?.use { inputStream ->
//            inputStream.readBytes()
//        } ?: return null
//        val requestBody = imageBytes.toRequestBody(mimeType.toMediaType())
//
//        return MultipartBody.Part.createFormData(
//            name = "avatar",
//            filename = contentResolver.getFileName(imageUri),
//            body = requestBody
//        )
//    }
//
//    private fun String.toPlainTextRequestBody(): RequestBody =
//        toRequestBody("text/plain".toMediaType())
//
//    private fun ContentResolver.getFileName(uri: Uri): String {
//        query(uri, null, null, null, null)?.use { cursor ->
//            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            if (nameIndex >= 0 && cursor.moveToFirst()) {
//                return cursor.getString(nameIndex)
//            }
//        }
//
//        return "avatar_${System.currentTimeMillis()}.jpg"
//    }

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