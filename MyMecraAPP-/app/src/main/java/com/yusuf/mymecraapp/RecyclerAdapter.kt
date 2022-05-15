package com.yusuf.mymecraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.tweets_ticket.view.*

class RecyclerAdapter(val postList :ArrayList<Tweet>) : RecyclerView.Adapter<RecyclerAdapter.PostHolder>(){
    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =inflater.inflate(R.layout.tweets_ticket,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.txt_tweet.text = postList[position].tweetText
        holder.itemView.txtUserName.text = postList[position].kullaniciemail
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}