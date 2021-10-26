package com.example.elderapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.util.Log






class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(resources.configuration);
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView?>(R.id.img_Logo)
        animateIn(logo)

        Handler().postDelayed({
            animateOut(logo)
            logo.setImageResource(R.drawable.ccu)
            animateIn(logo)
        }, 1500)
        Handler().postDelayed({
            val it = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(it)
            finish()
        }, 3000)


    }

    private fun animateIn(view: View?) {
        view!!.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
                .alpha(1f)
                .setDuration(1500)
                .setListener(null)
    }

    private fun animateOut(view: View?) {
        view!!.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = View.GONE
                    }
                })
    }

    private fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1.0) {
            Log.d("system",
                "fontScale=" + configuration.fontScale) //Custom Log class, you can use Log.w
            Log.d("system",
                "font too big. scale down...") //Custom Log class, you can use Log.w
            configuration.fontScale = 1.0f
            val metrics = resources.displayMetrics
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }
}