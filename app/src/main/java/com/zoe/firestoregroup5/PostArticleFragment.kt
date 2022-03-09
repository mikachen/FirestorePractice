package com.zoe.firestoregroup5

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.zoe.firestoregroup5.databinding.FragmentFirstBinding


class PostArticleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding = FragmentFirstBinding.inflate(inflater, container, false)
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("Articles")
        val spinner = binding.tagSpinner
        val arrayAdapter =
            ArrayAdapter.createFromResource(requireContext(), R.array.article_tag_array,
                androidx.appcompat.R.layout.select_dialog_item_material)

        var tag = ""
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


        binding.postButton.setOnClickListener {
            val id = ref.document().id
            val title = binding.edit1.text
            val content = binding.edit2.text

            val art = Article(
                id = id,
                title = title.toString(),
                content = content.toString(),
                tag = tag,
                created_time = Timestamp.now()
            )

            if (content.isNotEmpty() && title.isNotEmpty()) {
                ref.document(id)
                    .set(art)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Data added ", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, " Data not added ", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Please fill in title & content ", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        ref.addSnapshotListener { snapshot, e ->
            if (e != null) {
//                Log.w("Listen", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                Log.d("Listen", "Current data: ${snapshot.documents}")
            } else {
                Log.d("Listen", "Current data: null")
            }
        }

        binding.getButton.setOnClickListener {
            ref.orderBy("created_time", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener { it ->
                    if (it != null) {
                        Log.d("GET", "DocumentSnapshot it: $it")

                        val dataList = mutableListOf<String>()

                        for (element in it) {
                            val data = element.data
                            Log.d("GET", "DocumentSnapshot data: $data")
                            dataList.add("$data")
                        }
                        binding.docIdTv.text = "$dataList"
                    } else {
                        Log.d("GET", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("GET", "get failed with ", exception)
                    binding.docIdTv.text = "No document left: \n$exception"
                }
        }

        binding.deleteButton.setOnClickListener {
            val doc = binding.edit3.text.toString()
            db.collection("articles").document(doc)
                .delete()
                .addOnSuccessListener {
                    Log.d("DELETE", "DocumentSnapshot successfully deleted!")
                    binding.docIdTv.text = "$doc\nDELETE SUCCESS"
                }
                .addOnFailureListener { e -> Log.w("DELETE", "Error deleting document", e) }
        }


        return binding.root
    }
}
