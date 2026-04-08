package cst.unibucfmiif2026.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cst.unibucfmiif2026.data.AppDatabase
import cst.unibucfmiif2026.data.entities.AddressEntity
import cst.unibucfmiif2026.data.entities.UserEntity
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getInstance(application).userDao()
    private val addressDao = AppDatabase.getInstance(application).addressDao()
    fun addUser(firstname: String, lastname: String) {
       viewModelScope.launch {
           val addressId = addressDao.insert(AddressEntity(street = "abc", city = "abc", country = "RO"))
           val userId = userDao.insert(UserEntity(firstName = firstname, lastName = lastname, addressId = addressId))
           val result = userDao.getById(userId)
           Log.e("tag", "$result")
       }
    }
}