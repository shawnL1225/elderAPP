package com.example.elderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView logo = findViewById(R.id.img_Logo);
        animateIn(logo);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            animateOut(logo);
            logo.setImageResource(R.drawable.ccu);
            animateIn(logo);
        }, 1500);

        handler.postDelayed(() -> {
            Intent it = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }, 3000);

//        Intent it = new Intent(SplashActivity.this, LoginActivity.class);
//        startActivity(it);
//        finish();

    }

    private void animateIn(View view){
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
            .alpha(1f)
            .setDuration(1500)
            .setListener(null);



    }
    private void animateOut(View view){
        view.animate()
            .alpha(0f)
            .setDuration(1000)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }
            });
    }
}