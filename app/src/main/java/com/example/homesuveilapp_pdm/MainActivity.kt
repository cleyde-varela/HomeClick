package com.example.homesuveilapp_pdm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/* App's only and main activity (PB06) */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Title
        this.title = "HomeClick"
    }
}