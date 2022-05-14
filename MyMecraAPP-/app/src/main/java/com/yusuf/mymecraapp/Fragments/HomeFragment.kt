package com.yusuf.mymecraapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var database: FirebaseFirestore
    var tweetList = ArrayList<Tweet>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseFirestore.getInstance()

        verilerAl()
        var layoutManager = LinearLayoutManager(context)
    //    recycler_view_home.layoutManager = layoutManager
        recyclerViewAdapter = RecyclerAdapter(tweetList)
     //   recycler_view_home.adapter = recyclerViewAdapter
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    fun verilerAl() {
        database.collection("Post")
            // .orderBy("tarih", com.google.firebase.firestore.Query.Direction.DESCENDING)
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
                                println(kullanicitext)
                                val gorselurl = document.get("gorselurl") as String
                                val kullaniciemail = document.get("kullaniciemail") as String
                                val tarih = document.get("tarih").toString()
                                val tweet = Tweet(kullanicitext,gorselurl,kullaniciemail,tarih)
                                tweetList.add(tweet)
                            }
                            for (twet in tweetList){
                                println(twet.tweetText)
                                println(twet.kullaniciemail)
                                println(twet.gorselurl)
                                println(twet.tarih)
                            }
                            recyclerViewAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
    }



}
