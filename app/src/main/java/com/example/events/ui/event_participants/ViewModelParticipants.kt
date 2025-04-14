package com.example.events.ui.event_participants

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.events.data.entities.User

class ViewModelParticipants: ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private lateinit var allUser: List<User>

    fun initList(users: List<User>) {
        allUser = users
        _users.value = users
    }

    fun getUsers(query: String) {
        val sortedList = allUser.filter {
            it.clas?.contains(query, true) == true || it.name.contains(query, true)
        }
        _users.value = sortedList
    }

}