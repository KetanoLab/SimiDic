package com.ketanolab.simidic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : Activity() {
    private var imagenKetanolab: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        imagenKetanolab = findViewById<View>(R.id.imagenKetanolab) as ImageView
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        imagenKetanolab!!.startAnimation(animation)
    }
}