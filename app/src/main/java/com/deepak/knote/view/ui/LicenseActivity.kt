package com.deepak.knote.view.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*

class LicenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LicenseActivityUi().setContentView(this)
    }

    class LicenseActivityUi : AnkoComponent<LicenseActivity> {
        private val xmlStyles = { v: Any ->
            when (v) {
                is TextView -> v.textSize = 16f
            }
        }

        override fun createView(ui: AnkoContext<LicenseActivity>) = with(ui) {
            verticalLayout {
                padding = dip(8)
                textView("Kotlin By JetBrains")
                textView("Android Support Libraries AOSP")
                textView("Architecture Components by Google")
                textView("Anko By Kotlin")
                textView("MaterialAboutLibrary Daniel Stoneuk")
            }
        }.applyRecursively(xmlStyles)
    }
}
