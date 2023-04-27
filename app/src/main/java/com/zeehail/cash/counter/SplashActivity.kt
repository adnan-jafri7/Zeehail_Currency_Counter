package com.zeehail.cash.counter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

class SplashActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        if (ConnectionManager().checkConnectivity(this@SplashActivity)) {
            MobileAds.initialize(this@SplashActivity) {}
            mInterstitialAd = InterstitialAd(this@SplashActivity)
            mInterstitialAd.adUnitId = "ca-app-pub-6449044710108986/5197909645"
            mInterstitialAd.loadAd(AdRequest.Builder().build())}

            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            val imgLogo: ImageView = findViewById(R.id.imgLogo)
            val intent = Intent(this, MainActivity::class.java)
            val imgLogo1: ImageView = findViewById(R.id.imgLogo1)
            val animationSide = AnimationUtils.loadAnimation(this, R.anim.side_slide)
            val animTop = AnimationUtils.loadAnimation(this, R.anim.top_slide)
            imgLogo.startAnimation(animationSide)
            imgLogo1.startAnimation(animTop)


            Handler().postDelayed({
                if (ConnectionManager().checkConnectivity(this@SplashActivity)) {

                mInterstitialAd.adListener = object : AdListener() {
                    override fun onAdClosed() {
                        mInterstitialAd.loadAd(AdRequest.Builder().build())
                        startActivity(intent)
                    }
                }

                }
                if (ConnectionManager().checkConnectivity(this@SplashActivity)) {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    startActivity(intent)

                    /*Toast.makeText(
                        this@SplashActivity,
                        "The interstitial wasn't loaded yet.",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }}
                else{
                startActivity(intent)}

            }, 2000)


        }
    }