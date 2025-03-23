package com.example.events.ui.authorization.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class ViewModelRegistration: ViewModel() {

    private val fireStoreDb = FireStoreDb()

    private val _registrationResult = MutableLiveData<Resource<Any>>()
    val registrationResult: LiveData<Resource<Any>> = _registrationResult

    fun createAccount(user: User){
        viewModelScope.launch {
            _registrationResult.postValue(fireStoreDb.createUser(user))
        }
    }

}