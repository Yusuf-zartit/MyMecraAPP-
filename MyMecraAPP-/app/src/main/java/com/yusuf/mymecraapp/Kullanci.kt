package com.yusuf.mymecraapp

class Kullanci {
    var isim:String =""
    var user:User
    var profil:Byte
    var arkadas:Array<Kullanci>
    var tweet:Array<Tweet>

    constructor(isim:String,user:User,profil:Byte,arkadas:Array<Kullanci>,tweet:Array<Tweet>){
        this.isim=isim
        this.user=user
        this.profil=profil
        this.arkadas=arkadas
        this.tweet = tweet
    }
}