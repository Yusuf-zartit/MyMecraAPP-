package com.yusuf.mymecraapp.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.yusuf.mymecraapp.R
import kotlinx.android.synthetic.main.activity_ana_sayfa.*
import kotlinx.android.synthetic.main.activity_sign_page.*
import kotlinx.android.synthetic.main.activity_start_up.*
import kotlinx.android.synthetic.main.fragment_add.*
import androidx.appcompat.widget.AppCompatImageView;
class AddFragment : Fragment()  {

    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);

    }

//    fun click(view: View){
//    }
//
//    val READIMAGE:Int =123
//    fun checkPermission(view: View){
//        if(Build.VERSION.SDK_INT>=23){
//            if(ActivityCompat.checkSelfPermission(fragment_container.context,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
//                    PackageManager.PERMISSION_GRANTED){
//                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
//                return
//            }
//        }
//        loadImage()
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when(requestCode){
//            READIMAGE->{
//                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    loadImage()
//                }else{
//                    Toast.makeText(this.context,"Cannot access your image",Toast.LENGTH_LONG).show()
//                }
//            }else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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


//    val IMAGE_CODE:Int =123
//    fun loadImage(){
//        var intent=Intent(Intent.ACTION_PICK,
//        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent,IMAGE_CODE)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode==IMAGE_CODE && data!=null){
//            val selectedImage=data.data
//            val filePathColum= arrayOf(MediaStore.Images.Media.DATA)
//            val cursor= context?.contentResolver?.query(selectedImage!!,filePathColum,null,null,null)
//            cursor!!.moveToFirst()
//            val coulomIndex=cursor!!.getColumnIndex(filePathColum[0])
//            val picturePath=cursor!!.getString(coulomIndex)
//            cursor!!.close()
//            ivImagePerson.setImageBitmap(BitmapFactory.decodeFile(picturePath))        }
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && requestCode == Activity.RESULT_OK && data != null){
            secilenGorsel = data.data
            if(secilenGorsel != null){
                if (Build.VERSION.SDK_INT >= 28){
                    val contentResolver = requireActivity().contentResolver
                    val source = ImageDecoder.createSource(contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    ivImage.setImageBitmap(secilenBitmap)
                }else{
                    val contentResolver = requireActivity().contentResolver
                    secilenBitmap =MediaStore.Images.Media.getBitmap(contentResolver,secilenGorsel)
                    ivImage.setImageBitmap(secilenBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun gorselSec(view: View){
        if (ContextCompat.checkSelfPermission(fragment_container.context,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else{
            val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)
        }
    }
}