package com.deepak.knote.util

import android.support.v7.app.AppCompatActivity
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

fun AppCompatActivity.hideSoftKeyboard() {
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun AppCompatActivity.validateInput(title: String, content: String): Boolean {
    return !(TextUtils.isEmpty(title.trim()) && TextUtils.isEmpty(content.trim()))
}