package com.zoe.firestoregroup5.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.zoe.firestoregroup5.Article
import com.zoe.firestoregroup5.databinding.ArticleLinearViewBinding

class ArticlesAdapter(database: FirebaseFirestore) :
    ListAdapter<Article, ArticlesAdapter.ArticlesViewHolder>(DiffCall()) {

//    val ref = database.collection("Articles")


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ArticlesViewHolder {
        return ArticlesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {

        val data = getItem(position)
        holder.bind(data)

    }

    class ArticlesViewHolder(val binding: ArticleLinearViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(it: Article) {
            binding.title.text = it.title
            binding.time.text = it.created_time.toDate().toString()
            binding.author.text = it.author_id
            binding.tag.text = it.tag
            binding.content.text = it.content
        }


        companion object {
            fun from(parent: ViewGroup):ArticlesViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ArticleLinearViewBinding.inflate(layoutInflater, parent, false)

                return ArticlesViewHolder(binding)
            }
        }
    }
}

class DiffCall : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(
        oldItem: Article,
        newItem: Article,
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: Article,
        newItem: Article,
    ): Boolean {
        return oldItem == newItem
    }
}