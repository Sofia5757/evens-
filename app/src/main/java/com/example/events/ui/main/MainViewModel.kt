package com.example.events.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val fireStore = FireStoreDb()

    val myProfile = User()

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    fun getEvents() = viewModelScope.launch{
        _events.value = fireStore.getEvents()
    }

    fun cancelParticipation(event: Event){
        fireStore.cancelParticipation(event)
    }

}