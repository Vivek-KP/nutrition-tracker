package com.example.nutrition.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.nutrition.LoginActivity
import com.example.nutrition.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences =
            activity!!.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        val view: View = inflater.inflate(R.layout.fragment_user, container, false)
        val btnSignOut: Button = view.findViewById(R.id.btnSignout)


        btnSignOut.setOnClickListener {
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Confirm Signout")
            builder.setMessage("Are you sure you want to signout of this app?")
            builder.setPositiveButton(
                "Signout",
                DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    sharedPreferences.edit().clear().apply()
                    activity!!.finish()
                })
            builder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { _, _ -> })
            builder.show()
        }
        return view
    }
}