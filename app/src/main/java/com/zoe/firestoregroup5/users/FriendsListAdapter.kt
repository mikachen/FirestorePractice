package com.zoe.firestoregroup5.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.zoe.firestoregroup5.User
import com.zoe.firestoregroup5.databinding.FriendLinearViewBinding

class FriendsListAdapter(val database: FirebaseFirestore) :
    ListAdapter<User, FriendsListAdapter.FriendViewHolder>(DiffCall()) {

    val ref = database.collection("Users")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FriendViewHolder {
        return FriendViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {

        val view = getItem(position)
        holder.bind(view)
        holder.binding.acceptButton.setOnClickListener {
            acceptRequest(view.id)
            notifyDataSetChanged()
        }
        holder.binding.declineButton.setOnClickListener {
            declineRequest(view.id)
            notifyDataSetChanged()
        }
        holder.binding.cancelButton.setOnClickListener {
            declineRequest(view.id)
            notifyDataSetChanged()
        }
    }

    class FriendViewHolder(val binding: FriendLinearViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(view: User) {
            binding.name.text = view.name
            binding.UserId.text = view.id
            binding.mail.text = view.email

            if (!view.friends.isNullOrEmpty()) {
                when (view.friends["Zoe1018"]!!.toInt()) {
                    0 -> {
                        binding.cancelButton.visibility = View.INVISIBLE
                        binding.pendingButton.visibility = View.INVISIBLE
                        binding.declineButton.visibility = View.INVISIBLE
                        binding.acceptButton.visibility = View.INVISIBLE
                        binding.friendsButtonView.visibility = View.VISIBLE
                    }

                    1 -> {
                        binding.declineButton.visibility = View.VISIBLE
                        binding.acceptButton.visibility = View.VISIBLE
                        binding.friendsButtonView.visibility = View.INVISIBLE
                        binding.cancelButton.visibility = View.INVISIBLE
                        binding.pendingButton.visibility = View.INVISIBLE
                    }

                    2 -> {
                        binding.friendsButtonView.visibility = View.INVISIBLE
                        binding.cancelButton.visibility = View.VISIBLE
                        binding.pendingButton.visibility = View.VISIBLE
                    }
                }
            }
        }


        companion object {
            fun from(parent: ViewGroup): FriendViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FriendLinearViewBinding.inflate(layoutInflater, parent, false)

                return FriendViewHolder(binding)
            }
        }
    }

    fun declineRequest(userId: String) {
        ref.document("Zoe1018").update("friends.$userId", FieldValue.delete())
        ref.document(userId).update("friends.Zoe1018", FieldValue.delete())
    }

    fun acceptRequest(userId: String) {
        ref.document("Zoe1018").update("friends.$userId", 0)
        ref.document(userId).update("friends.Zoe1018", 0)
    }


}

class DiffCall : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(
        oldItem: User,
        newItem: User,
    ): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User,
    ): Boolean {
        return oldItem == newItem
    }
}