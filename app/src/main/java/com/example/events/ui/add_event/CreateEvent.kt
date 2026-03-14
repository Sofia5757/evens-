package com.example.events.ui.add_event

import com.example.events.data.entities.User
import java.util.Date

data class CreateEvent(
    var name: String? = null,
    var bonus: String? = null,
    var clas: String? = null,
    var accompanist: User? = null,
    var date: Date? = null,
    var place: String? = null,
    var description: String? = null
)
