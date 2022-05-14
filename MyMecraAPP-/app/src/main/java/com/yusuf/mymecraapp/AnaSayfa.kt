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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Query
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yusuf.mymecraapp.Fragments.*
import kotlinx.android.synthetic.main.activity_ana_sayfa.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_person.*
import java.sql.Timestamp
import java.time.Instant.now
import java.util.*

class AnaSayfa : AppCompatActivity() {
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val personFragment = PersonFragment()
    private val notificationsFragment = NotificationsFragment()
    private var addFragment = AddFragment()
    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap? = null
    //var kullaniciEmail:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_ana_sayfa)
    //    kullaniciEmail = intent.getStringExtra("email") as String
    //    verilerAl()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data
            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
//                    val contentResolver = requireActivity().contentResolver
                    val source = ImageDecoder.createSource(this.contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    ivImage.setImageBitmap(secilenBitmap)
          //          pro_image_profile_frag.setImageBitmap(secilenBitmap)
                } else {
//                    val contentResolver = requireActivity().contentResolver
                    secilenBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    ivImage.setImageBitmap(secilenBitmap)
      //              pro_image_profile_frag.setImageBitmap(secilenBitmap)
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
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val galeriIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent, 2)
        }
    }

    fun paylas(view: View) {
        println("geldi")
        val uuid = UUID.randomUUID()
        val gorselIsim = "${uuid}.jpg"
        val gorselReference = storage.reference.child("images").child(gorselIsim)
        if (secilenGorsel != null) {
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot ->
                val yuklenenGorselReference =
                    FirebaseStorage.getInstance().reference.child("images").child(gorselIsim)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                    val downlaodUrl = uri.toString()
                    val kullanicitext = etPost.text.toString()
                    var tarih = com.google.firebase.Timestamp.now()

                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("gorselurl", downlaodUrl)
     //               kullaniciEmail?.let { postHashMap.put("kullaniciemail", it) }
                    postHashMap.put("kullanicitext", kullanicitext)
                    postHashMap.put("tarih", tarih)

                    database.collection("Post").add(postHashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "başarıyla eklendi",
                                Toast.LENGTH_LONG
                            ).show()
                            replaceFragment(homeFragment)
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(
                            applicationContext,
                            exception.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun verilerAl(){
        database.collection("Post").orderBy("tarih",com.google.firebase.firestore.Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents
                        for (document in documents) {
                            val kullanicitext = document.get("kullanicitext") as String
                            val gorselurl = document.get("gorselurl") as String
                            val kullaniciemail = document.get("kullaniciemail") as String
                            val tarih = document.get("tarih") as String

                        }
                    }
                }
            }
        }
    }

//    fun profile (view: View){
//        val uuid = UUID.randomUUID()
//        val gorselIsim = "${uuid}.jpg"
//        val gorselReference = storage.reference.child("images").child(gorselIsim)
//        if (secilenGorsel != null) {
//            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot ->
//                val yuklenenGorselReference =
//                    FirebaseStorage.getInstance().reference.child("images").child(gorselIsim)
//                yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
//                    val downlaodUrl = uri.toString()
//                    val fullName = full_name_profile_frag.text.toString()
//                    val Bio = bio_name_profile_frag.text.toString()
//
//                    val postHashMap = hashMapOf<String, Any>()
//                    postHashMap.put("gorselurl", downlaodUrl)
//                    postHashMap.put("fullName", fullName)
//                    postHashMap.put("Bio", Bio)
//
//                    database.collection("Profile").add(postHashMap).addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(
//                                applicationContext,
//                                "başarıyla eklendi",
//                                Toast.LENGTH_LONG
//                            ).show()
//                            replaceFragment(homeFragment)
//                        }
//                    }.addOnFailureListener { exception ->
//                        Toast.makeText(
//                            applicationContext,
//                            exception.localizedMessage,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }.addOnFailureListener { exception ->
//                    Toast.makeText(
//                        applicationContext,
//                        exception.localizedMessage,
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//        }
//
//    }
}