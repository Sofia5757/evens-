package com.example.events.ui.event_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class ViewModelEventDetails: ViewModel() {

    private val fireStoreDb = FireStoreDb()

    private val _myProfile = MutableLiveData<Resource<User>>()
    val myProfile: LiveData<Resource<User>> = _myProfile

    private val _result = MutableLiveData<Resource<Unit>>()
    val result: LiveData<Resource<Unit>> = _result

    var event: Event? = null

    init {
        getProfile()
    }

    private fun getProfile()=viewModelScope.launch {
        _myProfile.value = fireStoreDb.getUser()
    }

    fun participateInEvent(withParent: Boolean) = viewModelScope.launch {
        event?.let {
            _result.value = fireStoreDb.participateInEvent(it, withParent)
        }
    }

    fun cancelParticipation() = viewModelScope.launch{
        event?.let {
            _result.value = fireStoreDb.cancelParticipation(it)
        }
    }

    fun cancelEvent() = viewModelScope.launch {
        event?.let {
            _result.value = fireStoreDb.cancelEvent(it)
        }
    }

}