package com.example.events.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class ViewModelUsers : ViewModel() {

    private val fireStore = FireStoreDb()

    private val _users = MutableLiveData<Resource<List<User>>>()
    val users: LiveData<Resource<List<User>>> = _users

    var curQuery = ""

    fun getUsers(query: String) = viewModelScope.launch {
        val response = fireStore.getUsers()
        curQuery = query
        when (response) {
            is Resource.Success -> {
                if (query.isNotEmpty()) {
                    response.data?.let { data ->
                        val sortedList = data.filter {
                            it.clas?.contains(query, true) == true || it.name.contains(query, true)
                        }
                        _users.postValue(Resource.Success(sortedList))
                    }
                }else{
                    _users.postValue(response)
                }
            }
            else -> _users.postValue(response)
        }
    }
}