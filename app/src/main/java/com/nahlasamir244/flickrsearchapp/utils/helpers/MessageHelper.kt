package com.nahlasamir244.flickrsearchapp.utils.helpers

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.nahlasamir244.flickrsearchapp.application.FlickrSearchApp

fun displayToast(textResourceId:Int) {
    Toast.makeText(FlickrSearchApp.getAppContext(),textResourceId,Toast.LENGTH_LONG).show()

}
fun displayToast(text:String) {
    Toast.makeText(FlickrSearchApp.getAppContext(),text,Toast.LENGTH_LONG).show()
}

fun displaySnackBar(view:View,textResourceId:Int) {
    Snackbar.make(view,textResourceId,Snackbar.LENGTH_LONG).show()
}