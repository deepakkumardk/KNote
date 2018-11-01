package com.deepak.knote.view.ui

import android.content.Context
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.deepak.knote.BuildConfig
import com.deepak.knote.R
import com.deepak.knote.util.GITHUB_ACCOUNT
import org.jetbrains.anko.browse
import org.jetbrains.anko.startActivity

/**
 * This tells about the app and the licenses using the MaterialAboutLibrary
 */
class AboutActivity : MaterialAboutActivity() {
    override fun getActivityTitle(): CharSequence? = "About"

    override fun getMaterialAboutList(context: Context): MaterialAboutList {
        val appCard = MaterialAboutCard.Builder()
        appCard.addItem(MaterialAboutTitleItem.Builder()
                .text("KNote")
                .desc("© 2018 Deepak Kumar")
                .icon(R.drawable.ic_knote)
                .build())
        appCard.addItem(MaterialAboutActionItem.Builder()
                .text("Version")
                .subText(BuildConfig.VERSION_NAME)
                .icon(R.drawable.ic_update)
                .build())
        appCard.addItem(MaterialAboutActionItem.Builder()
                .text("Licenses")
                .icon(R.drawable.ic_collections)
                .setOnClickAction { startActivity<LicenseActivity>() }
                .build())

        val developerCard = MaterialAboutCard.Builder()
        developerCard.addItem(MaterialAboutActionItem.Builder()
                .text("Developer")
                .subText("Deepak Kumar")
                .icon(R.drawable.ic_person_black)
                .build())
        developerCard.addItem(MaterialAboutActionItem.Builder()
                .text("GitHub")
                .subText(GITHUB_ACCOUNT)
                .icon(R.drawable.github_circle)
                .setOnClickAction { browse(GITHUB_ACCOUNT, true) }
                .build())

        return MaterialAboutList(appCard.build(), developerCard.build())
    }
}
