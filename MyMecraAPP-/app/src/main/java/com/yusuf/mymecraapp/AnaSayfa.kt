package com.yusuf.mymecraapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.yusuf.mymecraapp.Fragments.*
import kotlinx.android.synthetic.main.activity_ana_sayfa.*
import kotlinx.android.synthetic.main.fragment_add.*

class AnaSayfa : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val personFragment = PersonFragment()
    private val notificationsFragment = NotificationsFragment()
    private var addFragment = AddFragment()
    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_sayfa)
        val email = intent.getStringExtra("email")
        replaceFragment(homeFragment)
        buttom_nav_bar.setOnItemSelectedListener { id ->
            when (id) {
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

    fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    //    fun gorselSec(view: View){
//        if (ContextCompat.checkSelfPermission(fragment_container.context,
//                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
//        }else{
//            val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galeriIntent,2)
//        }
//    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1){

            if(grantResults.size > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && requestCode == Activity.RESULT_OK && data != null){
            secilenGorsel = data.data
            if(secilenGorsel != null){
                if (Build.VERSION.SDK_INT >= 28){
//                    val contentResolver = requireActivity().contentResolver
                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    ivImage.setImageBitmap(secilenBitmap)
                }else{
//                    val contentResolver = requireActivity().contentResolver
                    secilenBitmap =MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    ivImage.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun gorselSec(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            val galeriIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent, 2)
        }
    }
}