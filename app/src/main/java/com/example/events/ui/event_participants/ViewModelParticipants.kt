package com.example.events.ui.event_participants

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.FireStoreDb
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import kotlinx.coroutines.launch

class ViewModelParticipants : ViewModel() {

    private val fireStore = FireStoreDb()

    private val _users = MutableLiveData<Resource<List<User>>>()
    val users: LiveData<Resource<List<User>>> = _users

    private lateinit var allUser: List<User>

    fun initList(users: Array<String>, withParents: BooleanArray) = viewModelScope.launch {
        val data = fireStore.getParticipants(users)
        if (data is Resource.Success) {
            data.data?.let {
                allUser = it
                for (i in users.indices) {
                    it.find { it.id == users[i] }?.withParent = withParents[i]
                }
                _users.value?.data?.let {
                    allUser = it
                }
            }
        }
        _users.value = data
    }

    fun getUsers(query: String) {
        val sortedList = allUser.filter {
            it.clas?.contains(query, true) == true || it.name.contains(query, true)
        }
        _users.value = Resource.Success(sortedList)
    }

}