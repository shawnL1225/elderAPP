package com.example.elderapp

import android.app.Application
import android.view.View
import com.google.android.material.snackbar.Snackbar

object Global : Application() {
    var url: String? = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/"

    //    public static String url = "http://140.123.105.178/~tcus/elderApp/";
    fun putSnackBar(view: View?, s: String?) {
        val snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundResource(R.color.orange)
        snackbar.show()
    }

    fun putSnackBarR(view: View?, s: String?) {
        val snackbar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundResource(R.color.warning)
        snackbar.show()
    }
}