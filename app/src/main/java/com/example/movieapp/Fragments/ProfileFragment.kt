package com.example.movieapp.Fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.movieapp.Activities.LoginActivity
import com.example.movieapp.R
import com.example.movieapp.picture
import com.example.movieapp.userId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profilefragment.*
import java.io.File

class ProfileFragment : Fragment {

    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    var imageView: ImageView? = null
        private set

    constructor()
    private var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        database = FirebaseDatabase.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            picture=data?.data
            imageView?.setImageURI(data?.data)

            FirebaseDatabase.getInstance().getReference("users").child(userId).child("photo").setValue(
                picture.toString())
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.profilefragment, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val logoutBotton = view!!.findViewById<Button>(R.id.logoutButton)
        logoutBotton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            val intent = Intent (getActivity(), LoginActivity::class.java)
            getActivity()!!.startActivity(intent)
        }

        val updatePassword = view!!.findViewById<Button>(R.id.changePassword)
        updatePassword.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val txtNewPass = newPasswordEditText.text.toString()

            if (txtNewPass.isNotEmpty()){
                user!!.updatePassword(txtNewPass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context,"Password changed",Toast.LENGTH_SHORT).show()
                        println("Update Success")
                    } else {
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show()
                        println("Error Update")
                    }
                }
            }
        }

        val updateName = view!!.findViewById<Button>(R.id.updateName)
        updateName.setOnClickListener {
            if (nameEditText.text.toString().isNotEmpty()){
                FirebaseDatabase.getInstance().getReference("users").child(userId).child("name").setValue(nameEditText.text.toString())
            }

        }
        database!!.getReference("users").child(userId).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    val name = p0.getValue(String::class.java)
                    if (name != null) {
                        nameEditText.setText(name.toString())
                    }
                }

            })

        imageView = view!!.findViewById(R.id.imageView)

        database!!.getReference("users").child(userId).child("photo")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    val photo = p0.getValue(String::class.java)
                    if (photo != null) {
                        imageView?.setImageURI(Uri.parse(photo))
                    }
                }

            })

        imageView?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                   Log.v("No permission","Storage")
                } else {
                    pickImage()
                }

            } else {
                pickImage()
            }
        }


    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }





}
