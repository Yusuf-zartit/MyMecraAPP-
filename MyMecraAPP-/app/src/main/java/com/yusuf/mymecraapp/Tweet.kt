package com.yusuf.mymecraapp

import android.media.Image

class  Tweet{
    var tweetText:String?=null
    //    var image:Byte
    var gorselurl:String?=null
    var kullaniciemail:String?=null
    var tarih:String?=null
    constructor(kullanicitext:String,gorselurl:String,kullaniciemail:String,tarih:String){
        this.tweetText=kullanicitext
        this.gorselurl=gorselurl
        this.kullaniciemail=kullaniciemail
        this.tarih=tarih
    }
}