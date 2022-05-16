package com.yusuf.mymecraapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yusuf.mymecraapp.R
import com.yusuf.mymecraapp.RecyclerAdapter
import com.yusuf.mymecraapp.Tweet
import kotlinx.android.synthetic.main.activity_ana_sayfa.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

class HomeFragment : Fragment() {
    private lateinit var recyclerViewAdapter : RecyclerAdapter
//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<RecyclerAdapter.PostHolder>? = null
    private lateinit var database: FirebaseFirestore
    var tweetList = ArrayList<Tweet>()
    var ilksefer=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseFirestore.getInstance()
        val token = verilerAl()
        recyclerViewAdapter = RecyclerAdapter(token)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_home.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RecyclerAdapter(tweetList)
        }
    }


    fun verilerAl() :  ArrayList<Tweet>{
        database.collection("Post")
             .orderBy("tarih", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(fragment_container.context, exception.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {
                            val documents = snapshot.documents
                            tweetList.clear()
                            for (document in documents) {
                                val kullanicitext = document.get("kullanicitext") as String
                                val gorselurl = document.get("gorselurl") as String
                                val kullaniciemail = document.get("kullaniciemail") as String
                                val tarih = document.get("tarih").toString()
                                val tweet = Tweet(kullanicitext,gorselurl,kullaniciemail,tarih)
                                tweetList.add(tweet)
                            }
                            if(ilksefer===0){
                                recycler_view_home.apply {
                                    // set a LinearLayoutManager to handle Android
                                    // RecyclerView behavior
                                    layoutManager = LinearLayoutManager(activity)
                                    // set the custom adapter to the RecyclerView
                                    adapter = RecyclerAdapter(tweetList)
                                    ilksefer=1
                                }
                                recyclerViewAdapter = RecyclerAdapter(tweetList)
                            }

                            recyclerViewAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

        return tweetList
    }



}
