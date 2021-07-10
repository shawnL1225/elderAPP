package com.example.elderapp;

import android.app.Application;
import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Global extends Application {
    public static String url = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/";

    public static void putSnackBar(View view, String s){
        Snackbar snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundResource(R.color.orange);
        snackbar.show();
    }
    public static void putSnackBarR(View view, String s){
        Snackbar snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundResource(R.color.warning);
        snackbar.show();
    }

}
