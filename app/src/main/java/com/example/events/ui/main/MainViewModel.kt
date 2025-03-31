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
class EventViewModel : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    fun loadEvents() {

        val sampleEvents = listOf(
            Event("01/01/2025", "10:00", "Новогодний праздник", "Центральный парк", "Иванов И.И."),
            Event("02/02/2025", "14:00", "Встреча", "Кофейня", "Петров П.П.")
        )
        _events.value = sampleEvents
    }
}