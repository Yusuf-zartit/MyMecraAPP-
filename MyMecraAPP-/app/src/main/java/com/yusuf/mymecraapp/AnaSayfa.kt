package com.yusuf.mymecraapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yusuf.mymecraapp.Fragments.*
import kotlinx.android.synthetic.main.activity_ana_sayfa.*

class AnaSayfa : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val personFragment = PersonFragment()
    private val notificationsFragment = NotificationsFragment()
    private  var addFragment = AddFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_sayfa)
        val email= intent.getStringExtra("email")
        replaceFragment(homeFragment)
        buttom_nav_bar.setOnItemSelectedListener {  id->
            when(id){
                R.id.nav_home -> {
                    replaceFragment(homeFragment)
                }
                R.id.nav_search -> replaceFragment(searchFragment)
                R.id.nav_user -> replaceFragment(personFragment)
                R.id.nav_addPost -> replaceFragment(addFragment)
                R.id.nav_notifications -> replaceFragment(notificationsFragment)
            }
        }
    }

    fun replaceFragment(fragment: Fragment){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()
        }
    }
}