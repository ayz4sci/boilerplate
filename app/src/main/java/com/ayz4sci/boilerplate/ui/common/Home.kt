package com.ayz4sci.boilerplate.ui.common

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.ayz4sci.boilerplate.R
import com.ayz4sci.boilerplate.ui.viewHelpers.common.ChromeCustomTabsHelper
import com.google.android.material.snackbar.Snackbar
import com.ayz4sci.boilerplate.utils.CommonUtils
import kotlinx.android.synthetic.main.home.*


class Home : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    companion object {
        private const val ACTION_VIEW = "android.intent.action.VIEW"
        private val TAG = Home::class.java.simpleName
    }

    lateinit var fragmentSwitcher: FragmentSwitcherInterface
    lateinit var mChromeCustomTabsHelper: ChromeCustomTabsHelper
    private lateinit var fullScreenLoader: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        init()
    }

    private fun init() {
        fragmentSwitcher = FragmentSwitcher(this, supportFragmentManager)
        supportFragmentManager.addOnBackStackChangedListener(this)

        fullScreenLoader = findViewById(R.id.full_screen_loader)

        //Prepare and warm up Chrome Custom Tab
        mChromeCustomTabsHelper = ChromeCustomTabsHelper(this, "")

        fragmentSwitcher.showSplashScreen()

    }

    fun showFullScreenLoader() {
        CommonUtils.hideSoftKeyboard(this)
        fullScreenLoader.visibility = View.VISIBLE
    }

    fun hideFullScreenLoader() {
        fullScreenLoader.visibility = View.GONE
    }

    fun showMessage(message: String, isShort: Boolean = false) {
        hideFullScreenLoader()
        if (isShort) {
            Snackbar.make(coordinator_root_view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(coordinator_root_view, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onBackStackChanged() {
        try {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount

            if (backStackEntryCount > 0) {
                val backStackEntry = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1)
                // get tag of topmost fragment
                val topMostFragmentTag = backStackEntry.name
                if (topMostFragmentTag != null && topMostFragmentTag.isNotBlank()) {
                    val fragment = supportFragmentManager.findFragmentByTag(topMostFragmentTag) as BaseFragment
                    fragment.onResume()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount < 2) { // should not happen, just an exit condition
            finish()
        } else {
            if (fullScreenLoader.isShown) {
                // do nothing if full screen loader is visible
            } else {
                val fragment = supportFragmentManager.findFragmentById(R.id.home_full_screen_container)
                if (fragment != null && fragment is BaseFragment && !fragment.onBackPressed()) { //fragment has not handled the back press
                    try {
                        supportFragmentManager.popBackStackImmediate()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        mChromeCustomTabsHelper.finish()
        super.onDestroy()
    }
}