package com.example.events.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val fireStore = FireStoreDb()

    private val _userInfo = MutableLiveData<Resource<User>>()
    val userInfo: LiveData<Resource<User>> = _userInfo

    fun getUser() = viewModelScope.launch {
        _userInfo.value = fireStore.getUser()
    }

}