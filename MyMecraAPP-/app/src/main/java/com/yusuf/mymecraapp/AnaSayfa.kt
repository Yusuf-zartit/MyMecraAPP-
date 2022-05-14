package com.yusuf.mymecraapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Query
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yusuf.mymecraapp.Fragments.*
import kotlinx.android.synthetic.main.activity_ana_sayfa.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_person.*
import kotlinx.android.synthetic.main.fragment_person.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.sql.Timestamp
import java.time.Instant.now
import java.util.*
import kotlin.collections.ArrayList

class AnaSayfa : AppCompatActivity() {
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private val searchFragment = SearchFragment()
    private val personFragment = PersonFragment()
    private val homeFragment = HomeFragment()
    private val notificationsFragment = NotificationsFragment()
    private var addFragment = AddFragment()
    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap? = null
    var controlImage: Boolean = true
    var kullaniciEmail:String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_ana_sayfa)
        kullaniciEmail = intent.getStringExtra("email") as String
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
                    if (controlImage) {
                        ivImage.setImageBitmap(secilenBitmap)

                    } else {
                        pro_image_profile_frag.setImageBitmap(secilenBitmap)
                    }
                } else {
                    secilenBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    if (controlImage) {
                        ivImage.setImageBitmap(secilenBitmap)

                    } else {
                        pro_image_profile_frag.setImageBitmap(secilenBitmap)
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun gorselSec(view: View) {
        if (view.id == 2131230983) {//2131230983    add
            controlImage = true
        } else if (view.id == 2131231108) {// 2131231108 profile
            controlImage = false
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println(view.toString())
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
                    kullaniciEmail?.let { postHashMap.put("kullaniciemail", it) }
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

    fun profile(view: View) {
        val uuid = UUID.randomUUID()
        val gorselIsim = "${uuid}.jpg"
        val gorselReference = storage.reference.child("images").child(gorselIsim)
        if (secilenGorsel != null) {
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener { taskSnapshot ->
                val yuklenenGorselReference =
                    FirebaseStorage.getInstance().reference.child("images").child(gorselIsim)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener { uri ->
                    val downlaodUrl = uri.toString()
                    val username = profile_fragment_username.text.toString()
                    val email = auth.currentUser!!.email.toString()



                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("gorselurl", downlaodUrl)
                    postHashMap.put("username", username)
                    postHashMap.put("email", email)

                    database.collection("profile").document(email)
                        .addSnapshotListener { snapshot, exception ->
                            if (exception != null) {
                                Toast.makeText(
                                    applicationContext,
                                    exception.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                if (snapshot != null && snapshot.exists()) {

                                } else {
                                    println("else")
                                    database.collection("profile").parent
                                }

                            }
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
}