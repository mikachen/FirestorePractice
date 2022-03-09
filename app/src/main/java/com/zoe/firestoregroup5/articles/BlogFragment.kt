package com.zoe.firestoregroup5.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.zoe.firestoregroup5.R
import com.zoe.firestoregroup5.databinding.FragmentBlogBinding

class BlogFragment : Fragment() {

    lateinit var binding: FragmentBlogBinding

    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBlogBinding.inflate(inflater, container, false)

        val viewModel = ArticlesViewModel(db)
        val adapter = ArticlesAdapter(db)
        binding.articlesRecyclerView.adapter = adapter


        val arrayAdapter =
            ArrayAdapter.createFromResource(requireContext(), R.array.article_tag_array,
                androidx.appcompat.R.layout.select_dialog_item_material)
        var tag = ""
        val spinner = binding.tagSpinner
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> tag = "Beauty"
                    1 -> tag = "Gossiping"
                    2 -> tag = "SchoolLife"
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //輸入tag值 餵入查詢
        binding.searchIdButton.setOnClickListener {
            viewModel.toSearchWithTag(tag)
        }

        //監聽並顯示查詢結果 啟動recyclerView
        viewModel.articles.observe(viewLifecycleOwner){
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }






        return binding.root
    }
}