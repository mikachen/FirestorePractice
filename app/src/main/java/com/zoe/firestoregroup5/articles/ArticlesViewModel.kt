package com.zoe.firestoregroup5.articles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.zoe.firestoregroup5.Article

class ArticlesViewModel(db: FirebaseFirestore) : ViewModel() {

    val artsList = mutableListOf<Article>()
    val ref = db.collection("Articles")


    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>>
        get() = _articles


    //Get multiple documents from a collection
    fun toSearchWithTag(tag: String) {
        artsList.clear()

        ref.whereEqualTo("tag", tag)
            .get()
            .addOnSuccessListener {
                for (element in it) {
                    val article = Article(
                        id = element.data["id"].toString(),
                        tag = element.data["tag"].toString(),
                        title = element.data["title"].toString(),
                        content = element.data["content"].toString(),
                        author_id = element.data["author_id"].toString(),
                        created_time = element.data["created_time"] as Timestamp
                    )
                    artsList.add(article)
                }
                _articles.value = artsList
                Log.d("articles", "${_articles.value}")
            }
    }
}