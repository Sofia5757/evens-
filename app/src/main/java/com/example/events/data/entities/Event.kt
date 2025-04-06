package com.example.events.data.entities

import java.io.Serializable
import java.util.Date

data class Event(
    val name: String,
    val participants: List<User>,
    val accompanistName: String,
    val bonus: String,
    val date: Date,
    val place: String,
    val type: String,
    val description: String
): Serializable