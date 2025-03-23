package com.example.events.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.Event
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val fireStore = FireStoreDb()

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    fun getEvents() = viewModelScope.launch{
        _events.value = fireStore.getEvents()
    }

}