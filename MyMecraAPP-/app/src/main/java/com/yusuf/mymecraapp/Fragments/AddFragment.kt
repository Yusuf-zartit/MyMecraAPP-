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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);

    }

}