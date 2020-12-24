package com.example.nutrition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.nutrition.fragments.AssignFragment
import com.example.nutrition.fragments.CameraFragment
import com.example.nutrition.fragments.HomeFragment
import com.example.nutrition.fragments.UserFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle("Nutrition Tracker")
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        val homeFragment = HomeFragment()
        val assignFragment = AssignFragment()
        val cameraFragment = CameraFragment()
        val userFragment = UserFragment()
        makeCurrentFrackment(homeFragment)
        bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> makeCurrentFrackment(homeFragment)
                R.id.ic_assignment -> makeCurrentFrackment(assignFragment)
                R.id.ic_camera -> makeCurrentFrackment(cameraFragment)
                R.id.ic_account -> makeCurrentFrackment(userFragment)
            }
            true
        }
    }

    private fun makeCurrentFrackment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }


}


