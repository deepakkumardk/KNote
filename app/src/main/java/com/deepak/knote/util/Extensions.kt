package com.deepak.knote.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
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

fun FragmentActivity.startActivityForResults(intent: Intent, requestCode: Int, activity: Activity) {
    intent.putExtra(EXTRA_RC, requestCode)
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity)
        this.startActivityForResult(intent, requestCode, options.toBundle())
    } else {
        this.startActivityForResult(intent, requestCode)
    }
}

fun setBackground(item: View, dX: Float): ColorDrawable? {
    //Draw the background of note item with material red color set the bounds for background of item
    val background = ColorDrawable(Color.parseColor("#f44336"))
    background.setBounds(item.left, item.top, (item.left + dX).toInt(), item.bottom)
    return background
}

fun getDeleteIcon(item: View, icon: Drawable?): Drawable? {
    //Draw the icon over the background
    val iconWidth = icon?.intrinsicWidth as Int
    val iconHeight = icon.intrinsicHeight
    val cellHeight = item.bottom - item.top
    val margin = (cellHeight - iconHeight) / 2
    val iconTop = item.top + margin
    val iconBottom = iconTop + iconHeight
    val iconLeft = 48
    val iconRight = iconLeft + iconWidth

    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
    return icon
}