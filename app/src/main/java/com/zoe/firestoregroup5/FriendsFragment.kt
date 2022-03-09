package com.zoe.firestoregroup5

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.zoe.firestoregroup5.databinding.FragmentFriendsBinding
import com.zoe.firestoregroup5.users.FriendsListAdapter
import com.zoe.firestoregroup5.users.FriendsViewModel

class FriendsFragment : Fragment() {

    lateinit var mail: String
    lateinit var binding: FragmentFriendsBinding
    lateinit var adapter: FriendsListAdapter
    lateinit var viewModel: FriendsViewModel

    val db = FirebaseFirestore.getInstance()
    val ref = db.collection("Users")
    val friendsIdList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFriendsBinding.inflate(inflater, container, false)
        adapter = FriendsListAdapter(db)
        viewModel = FriendsViewModel(db)
        binding.friendListRecyclerView.adapter = adapter

        //監聽我當前所有的朋友清單，啟動recyclerView
        viewModel.friendsList.observe(viewLifecycleOwner) {
            Log.d("updateList","${it.isNullOrEmpty()}")
            if (it.isNullOrEmpty()) {
                binding.friendListRecyclerView.visibility = View.GONE
            } else {
                binding.friendListRecyclerView.visibility = View.VISIBLE
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        }

        //點擊加好友後，關掉按鈕
        viewModel.isUserInList.observe(viewLifecycleOwner){
            Log.d("isUserInList","$it")
            if(it){
                binding.addFriendButton.visibility = View.INVISIBLE
            }else{
                binding.addFriendButton.visibility = View.VISIBLE
            }
        }

        viewModel.newInvitations.observe(viewLifecycleOwner){
            Log.d("it.isNullOrEmpty()","${it.isNullOrEmpty()}")
            if(!it.isNullOrEmpty()){
                Toast.makeText(context, "You have a new invitation!", Toast.LENGTH_SHORT).show()
            }
        }

        //查詢Mail
        binding.searchButton.setOnClickListener {
            mail = binding.searchMailEdit.text.toString()
            viewModel.searchMail(mail)
        }

        //顯示查詢結果
        viewModel.searchResult.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.userInfoTv.text =
                    getString(R.string.search_result, it.email, it.name, it.id)

                Log.d("contain","${it.friends?.containsKey("Zoe1018")}")
                if (it.friends.isNullOrEmpty() || !it.friends?.containsKey("Zoe1018")) {
                    binding.addFriendButton.visibility = View.VISIBLE
                }else{
                    binding.addFriendButton.visibility = View.INVISIBLE
                }
            }
        }

        //添加好友
        binding.addFriendButton.setOnClickListener {
            viewModel.sendFriendRequest()
        }


        //addSnap監聽
        ref.document("Zoe1018").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Listen", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                friendsIdList.clear()

                /** 撈出自己文件 friends Map<> */
                snapshot["friends"]?.let {
                    for (element in it as MutableMap<String, Int>) {
                        friendsIdList.add(element.key)
                    }
                }
                /** 依照idList Query User資料 */
                viewModel.getUserInfo(friendsIdList)

            } else {
                Log.d("Listen", "Current data: null")
            }
        }

        return binding.root

    }
}


