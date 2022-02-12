package com.nahlasamir244.flickrsearchapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nahlasamir244.flickrsearchapp.R
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.ads.initialization.InitializationStatus

import com.google.android.gms.ads.initialization.OnInitializationCompleteListener

import com.google.android.gms.ads.MobileAds




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeAds()
    }
    fun initializeAds(){
        MobileAds.initialize(
            this, OnInitializationCompleteListener {
                initializationStatus ->
            })
    }
}