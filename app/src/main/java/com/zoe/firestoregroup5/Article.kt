package com.zoe.firestoregroup5

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class Article(
    val id: String,
    val title: String,
    val content: String,
    val tag: String,
    val author_id: String = "Zoe1018",
    val created_time: Timestamp,
)