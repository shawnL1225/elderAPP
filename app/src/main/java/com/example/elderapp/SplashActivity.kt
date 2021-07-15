package com.example.elderapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val logo = findViewById<ImageView?>(R.id.img_Logo)
        animateIn(logo)
        val handler = Handler()
        handler.postDelayed({
            animateOut(logo)
            logo.setImageResource(R.drawable.ccu)
            animateIn(logo)
        }, 1500)
        handler.postDelayed({
            val it = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(it)
            finish()
        }, 3000)

//        Intent it = new Intent(SplashActivity.this, LoginActivity.class);
//        startActivity(it);
//        finish();
    }

    private fun animateIn(view: View?) {
        view.setAlpha(0f)
        view.setVisibility(View.VISIBLE)
        view.animate()
                .alpha(1f)
                .setDuration(1500)
                .setListener(null)
    }

    private fun animateOut(view: View?) {
        view.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.setVisibility(View.GONE)
                    }
                })
    }
}