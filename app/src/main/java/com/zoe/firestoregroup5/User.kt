package com.zoe.firestoregroup5

data class User(
    val id: String,
    val name: String,
    val friends: Map<String, Int>?,
    val email: String,
)