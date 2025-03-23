package com.example.events.ui.profile_settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class ViewModelSettings: ViewModel() {

    private val fireStoreDb = FireStoreDb()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _editResult = MutableLiveData<Resource<Any>>()
    val editResult: LiveData<Resource<Any>> = _editResult

    fun editAccount(user: User){
        viewModelScope.launch {
            _editResult.postValue(fireStoreDb.createUser(user))
        }
    }

    fun setUser(user: User){
        _user.value = user
    }

}