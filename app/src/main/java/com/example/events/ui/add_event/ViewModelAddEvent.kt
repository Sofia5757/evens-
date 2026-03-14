package com.example.events.ui.add_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.utils.Resource
import kotlinx.coroutines.launch
import java.util.Date

class ViewModelAddEvent: ViewModel() {

    private val fireStore = FireStoreDb()

    private val _result = MutableLiveData<Resource<Unit>>()
    val result: LiveData<Resource<Unit>> = _result

    private val _isPostBtEnabled = MutableLiveData<Boolean>()
    val isPostBtEnabled: LiveData<Boolean> = _isPostBtEnabled

    private val createEvent = CreateEvent()

    fun createEvent() = viewModelScope.launch{
        val data = fireStore.getUser()
        if(data is Resource.Success){
            createEvent.accompanist = data.data
            _result.value = fireStore.createEvent(createEvent)
        }
    }

    fun enterName(value: String){
        createEvent.name = value
        validateEventInfo()
    }

    fun enterDescription(value: String){
        createEvent.description = value
    }

    fun enterPlace(value: String){
        createEvent.place = value
        validateEventInfo()
    }

    fun enterBonus(value: String){
        createEvent.bonus = value
    }

    fun enterClas(value: String){
        createEvent.clas = value
    }

    fun enterDate(value: Date){
        createEvent.date = value
        validateEventInfo()
    }

    private fun validateEventInfo(){
        _isPostBtEnabled.value = !createEvent.name.isNullOrEmpty() &&
                !createEvent.place.isNullOrEmpty() &&
                createEvent.date != null
    }
}