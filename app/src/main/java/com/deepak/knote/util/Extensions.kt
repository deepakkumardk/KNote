package com.deepak.knote.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import org.jetbrains.anko.inputMethodManager

/**
 * Extension functions
 */
fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.hide() {
    this?.visibility = View.GONE
}

fun RecyclerView.init(context: Context) {
    this.apply {
        hasFixedSize()
        layoutManager = LinearLayoutManager(context)
    }
}

fun AppCompatActivity.hideSoftKeyboard() {
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun AppCompatActivity.validateInput(title: String, content: String): Boolean {
    return !(TextUtils.isEmpty(title.trim()) && TextUtils.isEmpty(content.trim()))
}

fun View.setAlphaAnimation(dX: Float) {
    //set the alpha animation on swipe
    val alphaAnimation = 1.0f - Math.abs(dX) / this.width.toFloat()
    this.alpha = alphaAnimation
    this.translationX = dX
}

fun FragmentActivity.startActivityForResults(intent: Intent, requestCode: Int, activity: Activity) {
    intent.putExtra(RC_ACTIVITY, requestCode)
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity)
        this.startActivityForResult(intent, requestCode, options.toBundle())
    } else {
        this.startActivityForResult(intent, requestCode)
    }
}