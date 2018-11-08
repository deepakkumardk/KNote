package com.deepak.knote.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
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

fun androidx.recyclerview.widget.RecyclerView.init(context: Context) {
    this.apply {
        hasFixedSize()
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }
}

fun AppCompatActivity.hideSoftKeyboard() {
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun validateInput(title: String, content: String): Boolean {
    return !(TextUtils.isEmpty(title.trim()) && TextUtils.isEmpty(content.trim()))
}

fun View.setAlphaAnimation(dX: Float) {
    //set the alpha animation on swipe
    val alphaAnimation = 1.0f - Math.abs(dX) / this.width.toFloat()
    this.alpha = alphaAnimation
    this.translationX = dX
}

fun androidx.fragment.app.FragmentActivity.startActivityForResults(intent: Intent, requestCode: Int, activity: Activity) {
    intent.putExtra(RC_ACTIVITY, requestCode)
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity)
        this.startActivityForResult(intent, requestCode, options.toBundle())
    } else {
        this.startActivityForResult(intent, requestCode)
    }
}