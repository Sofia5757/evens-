package com.example.events.data.entities

import java.io.Serializable

data class User(
    val name: String,
    val clas: String?,
    val teacher: Boolean,
    val email: String
): Serializable{
    constructor() : this(
        "",
        null,
        false,
        ""
    )
}

data class UserList(
    val users: List<User>
): Serializable
