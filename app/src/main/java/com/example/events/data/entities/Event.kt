package com.example.events.data.entities

import java.io.Serializable
import java.util.Date

data class Event(
    var id: String,
    val name: String,
    val participants: List<String>,
    val withParent: List<Boolean>,
    val accompanistId: String,
    val accompanistName: String,
    val bonus: String?,
    val date: Date,
    val place: String,
    val description: String?,
    val clas: String?
): Serializable{
    constructor(): this(
        "",
        "",
        listOf(),
        listOf(),
        "",
        "",
        null,
        Date(),
        "",
        null,
        null
    )
}