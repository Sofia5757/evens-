package com.example.events.data.entities

import java.io.Serializable

data class User(
    var id: String,
    val name: String,
    val clas: String?,
    val teacher: Boolean,
    val email: String
): Serializable{
    constructor() : this(
        "",
        "",
        null,
        false,
        ""
    )
    constructor(
        name: String,
        clas: String,
        teacher: Boolean,
        email: String
    ) : this(
        "",
        name,
        clas,
        teacher,
        email
    )

    var withParent: Boolean? = null
}
