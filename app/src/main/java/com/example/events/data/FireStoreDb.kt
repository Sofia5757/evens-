package com.example.events.data

import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import com.example.events.ui.add_event.CreateEvent
import com.example.events.utils.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FireStoreDb {

    private val db = Firebase.firestore
    val myProfile = FirebaseAuth.getInstance()

    suspend fun getEvents(): Resource<List<Event>> {
        return suspendCoroutine<Resource<List<Event>>> { cont ->
            db.collection("events")
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty && result != null) {
                        cont.resume(Resource.Success(result.toObjects(Event::class.java).filter {
                            it.date>Date()
                        }.sortedBy { it.date }))
                    } else {
                        cont.resume(Resource.Success(listOf()))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

    suspend fun getUsers(): Resource<List<User>> {
        return suspendCoroutine<Resource<List<User>>> { cont ->
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty && result != null) {
                        cont.resume(Resource.Success(result.toObjects(User::class.java)))
                    } else {
                        cont.resume(Resource.Success(listOf()))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

    suspend fun getParticipants(ids: Array<String>): Resource<List<User>> {
        return suspendCoroutine<Resource<List<User>>> { cont ->
            db.collection("users")
                .whereIn("id", ids.toList())
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty && result != null) {
                        cont.resume(Resource.Success(result.toObjects(User::class.java)))
                    } else {
                        cont.resume(Resource.Success(listOf()))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

    suspend fun getUser(): Resource<User> {
        return suspendCoroutine<Resource<User>> { cont ->
            myProfile.uid?.let {
                db.collection("users").document(it)
                    .get()
                    .addOnSuccessListener { result ->
                        cont.resume(Resource.Success(result.toObject<User>()))
                    }
                    .addOnFailureListener { exception ->
                        cont.resume(Resource.Error(exception))
                    }
            }
        }
    }

    suspend fun getMyEvents(user: User): Resource<List<Event>> {
        return suspendCoroutine<Resource<List<Event>>> { cont ->
            db.collection("events")
                .where(
                    Filter.or(
                        Filter.arrayContains("participants", user.id),
                        Filter.equalTo("accompanistId", user.id)
                    )
                )
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty && result != null) {
                        cont.resume(Resource.Success(result.toObjects(Event::class.java).filter {
                            it.date>Date()
                        }.sortedBy { it.date }))
                    } else {
                        cont.resume(Resource.Success(listOf()))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

    suspend fun getClassEvents(user: User): Resource<List<Event>> {
        return suspendCoroutine<Resource<List<Event>>> { cont ->
            db.collection("events")
                .whereEqualTo("clas", user.clas)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty && result != null) {
                        cont.resume(Resource.Success(result.toObjects(Event::class.java).filter {
                            it.date>Date()
                        }.sortedBy { it.date }))
                    } else {
                        cont.resume(Resource.Success(listOf()))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

    suspend fun createUser(user: User): Resource<Any> {
        return suspendCoroutine { cont ->
            myProfile.uid?.let {
                db.collection("users")
                    .document(it)
                    .set(user.apply {
                        id = it
                    })
                    .addOnSuccessListener {
                        cont.resume(Resource.Success(null))
                    }
                    .addOnFailureListener { exception ->
                        cont.resume(Resource.Error(exception))
                    }
            }
        }
    }

    suspend fun createEvent(createEvent: CreateEvent): Resource<Unit> {
        val event = Event(
            id = "",
            name = createEvent.name!!,
            participants = listOf<String>(),
            withParent = listOf<Boolean>(),
            accompanistId = createEvent.accompanist?.id!!,
            accompanistName = createEvent.accompanist?.name!!,
            bonus = createEvent.bonus,
            date = createEvent.date!!,
            description = createEvent.description,
            place = createEvent.place!!,
            clas = createEvent.clas
        )
        return suspendCoroutine { cont ->
            db.collection("events")
                .document().let { doc ->
                    doc.set(event.apply {
                        id = doc.id
                    })
                        .addOnSuccessListener {
                            cont.resume(Resource.Success(null))
                        }
                        .addOnFailureListener { exception ->
                            cont.resume(Resource.Error(exception))
                        }
                }

        }
    }

    suspend fun participateInEvent(event: Event, withParent: Boolean): Resource<Unit> {
        return suspendCoroutine { cont ->
            myProfile.uid?.let {
                val newEvent = event.copy(
                    participants = event.participants.toMutableList().apply{add(it)},
                    withParent = event.withParent.toMutableList().apply {add(withParent)}
                )
                db.collection("events")
                    .document(event.id)
                    .set(newEvent)
                    .addOnSuccessListener {
                        cont.resume(Resource.Success(null))
                    }
                    .addOnFailureListener { exception ->
                        cont.resume(Resource.Error(exception))
                    }
            }
        }
    }

    suspend fun cancelParticipation(event: Event): Resource<Unit> {
        val index = event.participants.indexOf(myProfile.uid)
        val newEvent = event.copy(
            participants = event.participants.toMutableList().apply {
                removeAt(index)
            },
            withParent = event.withParent.toMutableList().apply {
                removeAt(index)
            }
        )
        return suspendCoroutine { cont ->
            db.collection("events")
                .document(event.id)
                .set(newEvent)
                .addOnSuccessListener {
                    cont.resume(Resource.Success(null))
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

    suspend fun cancelEvent(event: Event): Resource<Unit> {
        return suspendCoroutine { cont ->
            db.collection("events")
                .document(event.id)
                .delete()
                .addOnSuccessListener {
                    cont.resume(Resource.Success(null))
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

}