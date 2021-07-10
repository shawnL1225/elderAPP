package com.example.elderapp;

import android.app.Application;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Global extends Application {
    public static String url = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/";

    public static void putSnackBar(View view, String s){
        Snackbar snackbar = Snackbar.make(view, "已設置新地點", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundResource(R.color.orange);
        snackbar.show();
    }

}
