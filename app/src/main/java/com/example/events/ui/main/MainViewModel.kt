package com.example.events.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val fireStore = FireStoreDb()

    var myProfile: User? = null

    private val _events = MutableLiveData<Resource<List<Event>>>()
    val events: LiveData<Resource<List<Event>>> = _events

    private val _result = MutableLiveData<Resource<Unit>>()
    val result: LiveData<Resource<Unit>> = _result

    var curQuery = ""

    fun getEvents(query: String) = viewModelScope.launch{
        val user = fireStore.getUser()
        if(user is Resource.Success) {
            myProfile = user.data
            val data = fireStore.getEvents()
            when (data) {
                is Resource.Success -> {
                    if (query.isNotEmpty()) {
                        data.data?.let { data ->
                            val sortedList = data.filter {
                                it.name.contains(query, true)
                            }
                            _events.postValue(Resource.Success(sortedList))
                        }
                    }else{
                        _events.postValue(data)
                    }
                }
                else -> _events.postValue(data)
            }
        }

    }

    fun cancelParticipation(event: Event) = viewModelScope.launch{
        _result.value = fireStore.cancelParticipation(event)
    }

    fun cancelEvent(event: Event) = viewModelScope.launch {
        _result.value = fireStore.cancelEvent(event)
    }

}