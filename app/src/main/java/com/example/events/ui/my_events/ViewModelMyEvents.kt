package com.example.events.ui.my_events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class ViewModelMyEvents : ViewModel() {

    private val fireStore = FireStoreDb()

    private val _events = MutableLiveData<Resource<List<Event>>>()
    val events: LiveData<Resource<List<Event>>> = _events

    private val _userInfo = MutableLiveData<Resource<User>>()
    val userInfo: LiveData<Resource<User>> = _userInfo
    
    fun getProfile() = viewModelScope.launch {
        _userInfo.value = fireStore.getUser()
    }

    fun getEvents() = viewModelScope.launch {
        userInfo.value?.data?.let {
            _events.value = fireStore.getMyEvents(it)
        }
    }
}