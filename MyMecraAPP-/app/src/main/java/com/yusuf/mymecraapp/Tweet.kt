package com.yusuf.mymecraapp

import android.media.Image

class  Tweet{
    var tweetID:String?=null
    var tweetText:String?=null
    //    var image:Byte
    var tweetImageURL:String?=null
    var tweetPersonUID:String?=null
    constructor(tweetID:String,tweetText:String,tweetImageURL:String,tweetPersonUID:String){
        this.tweetID=tweetID
        this.tweetText=tweetText
        this.tweetImageURL=tweetImageURL
        this.tweetPersonUID=tweetPersonUID
    }
}