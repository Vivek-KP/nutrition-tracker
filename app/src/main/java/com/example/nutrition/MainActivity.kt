package com.example.nutrition


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.nutrition.fragments.AssignFragment
import com.example.nutrition.fragments.CameraFragment
import com.example.nutrition.fragments.HomeFragment
import com.example.nutrition.fragments.UserFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerLocalModel
import com.google.mlkit.vision.label.automl.AutoMLImageLabelerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import java.io.File
import java.util.jar.Manifest



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         val homeFragment = HomeFragment()
        val assignFragment = AssignFragment()
        val cameraFragment = CameraFragment()
        val userFragment = UserFragment()
        makeCurrentFrackment(homeFragment)
        bottomnav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home-> makeCurrentFrackment(homeFragment)
                R.id.ic_assignment-> makeCurrentFrackment(assignFragment)
                R.id.ic_camera-> makeCurrentFrackment(cameraFragment)
                R.id.ic_account-> makeCurrentFrackment(userFragment)
            }
            true
        }
        }

    private fun makeCurrentFrackment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment)
            commit()
        }


}


