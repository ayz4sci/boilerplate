package com.ayz4sci.boilerplate.ui.viewHelpers.common

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import com.ayz4sci.boilerplate.R


class ChromeCustomTabsHelper(val context: Context, val myLaunchUri: String? = null) {
    private val customTabPackageName = "com.android.chrome"
    private var customTabsIntent: CustomTabsIntent?
    private var mCustomTabsClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null
    val connection: CustomTabsServiceConnection

    init {
        connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(name: ComponentName?, client: CustomTabsClient?) {
                mCustomTabsClient = client
                mCustomTabsClient?.let {
                    it.warmup(0L)
                    mCustomTabsSession = it.newSession(null)
                }
                mCustomTabsSession?.mayLaunchUrl(Uri.parse(myLaunchUri ?: ""), null, null)
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                mCustomTabsClient = null
            }
        }

        CustomTabsClient.bindCustomTabsService(context, customTabPackageName, connection)

        customTabsIntent = CustomTabsIntent.Builder(mCustomTabsSession)
                .setToolbarColor(ContextCompat.getColor(context, R.color.color_primary))
                .setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.color_primary_dark))
                .setShowTitle(true)
                .build()
    }

    fun launchURL(uri: String) = customTabsIntent?.launchUrl(context, Uri.parse(uri))

    fun finish() = context.unbindService(connection)
}
