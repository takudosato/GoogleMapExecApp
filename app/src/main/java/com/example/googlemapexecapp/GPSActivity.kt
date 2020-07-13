package com.example.googlemapexecapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class GPSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity onCreate", "in")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_p_s)

        Log.d("MainActivity onCreate", "out")
    }
}