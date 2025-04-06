package com.example.events.data

import com.example.events.data.entities.Event
import com.example.events.data.entities.User
import com.example.events.utils.Resource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FireStoreDb {

    private val db = Firebase.firestore
    val myProfile = FirebaseAuth.getInstance()

    suspend fun getEvents(): List<Event> {
        return suspendCoroutine<List<Event>> { cont ->
            val events = db.collection("events")
            events.get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty && result != null) {
                        cont.resume(result.toObjects(Event::class.java))
                    } else {
                        cont.resume(listOf())
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resumeWithException(exception)
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
                    }else{
                        cont.resume(Resource.Success(listOf()))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Resource.Error(exception))
                }
        }
    }

    suspend fun getUser(): Resource<User>? {
        return suspendCoroutine<Resource<User>?> { cont ->
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

    fun getMyEvents(id: String) {

    }

    suspend fun createUser(user: User): Resource<Any> {
        return suspendCoroutine { cont ->
            myProfile.uid?.let {
                db.collection("users")
                    .document(it)
                    .set(user)
                    .addOnSuccessListener {
                        cont.resume(Resource.Success(null))
                    }
                    .addOnFailureListener { exception ->
                        cont.resume(Resource.Error(exception))
                    }
            }
        }
    }

    fun updateUser(user: User) {

    }

    fun createEvent(event: Event) {

    }

    fun participateInEvent(event: Event, id: String) {

    }

    fun cancelParticipation(event: Event) {

    }

}