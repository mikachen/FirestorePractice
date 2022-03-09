package com.zoe.firestoregroup5.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.zoe.firestoregroup5.User

class FriendsViewModel(db: FirebaseFirestore) : ViewModel() {
    val list = mutableListOf<User>()
    val newInviter = mutableListOf<String>()
    val ref = db.collection("Users")

    private val _friendsList = MutableLiveData<List<User>>()
    val friendsList: LiveData<List<User>>
        get() = _friendsList

    private val _searchResult = MutableLiveData<User>()
    val searchResult: LiveData<User>
        get() = _searchResult

    private val _isUserInList = MutableLiveData<Boolean>()
    val isUserInList: LiveData<Boolean>
        get() = _isUserInList

    private val _newInvitations = MutableLiveData<List<String>>()
    val newInvitations: LiveData<List<String>>
        get() = _newInvitations

    fun getUserInfo(friendsIdList: List<String>) {
        list.clear()
        newInviter.clear()

        if (friendsIdList.isNullOrEmpty()) {
            _friendsList.value = list
            _isUserInList.value = false
        } else {
            for (element in friendsIdList) {
                ref.document(element).get().addOnSuccessListener {
                    if (it != null) {
                        val user = User(
                            id = it["id"].toString(),
                            name = it["name"].toString(),
                            email = it["email"].toString(),
                            friends = it["friends"] as Map<String, Int>?
                        )
                        list.add(user)
                        _friendsList.value = list

                        Log.d("others","${it["friends.Zoe1018"].toString() == "1"}")
                        if(it["friends.Zoe1018"].toString() == "1"){
                            Log.d("who Invite Me","${it["id"]}")
                            newInviter.add(it["name"].toString())
                            _newInvitations.value = newInviter
                        }
                    }
                }
            }
            _isUserInList.value = true
        }
    }


    fun searchMail(mail: String) {
        ref.whereEqualTo("email", mail)
            .get()
            .addOnSuccessListener { it ->
                if (it != null) {
                    for (element in it) {
                        val searchUser = User(
                            id = element["id"].toString(),
                            name = element["name"].toString(),
                            email = element["email"].toString(),
                            friends = element["friends"] as Map<String, Int>?
                        )
                        _searchResult.value = searchUser
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("GET", "get failed with ", exception)
            }
    }

    fun sendFriendRequest() {
        val requestData = hashMapOf(searchResult.value!!.id to 1)
        val merge = hashMapOf("friends" to requestData)

        val syncData = hashMapOf("Zoe1018" to 2)
        val mergeSyncData = hashMapOf("friends" to syncData)

        ref.document("Zoe1018").set(merge, SetOptions.merge())
        ref.document(searchResult.value!!.id).set(mergeSyncData, SetOptions.merge())

    }
}