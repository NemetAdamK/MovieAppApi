package com.example.movieapp

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profilefragment.*

var userId: String = ""
var userName = ""
var picture: Uri? = Uri.parse("")
var myPreferences = "myPrefs"
var booleanIsFavorite = false