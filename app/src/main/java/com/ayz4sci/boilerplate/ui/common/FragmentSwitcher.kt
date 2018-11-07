package com.ayz4sci.boilerplate.ui.common

import androidx.fragment.app.FragmentManager
import com.ayz4sci.boilerplate.R
import com.ayz4sci.boilerplate.ui.main.MainFragment
import com.ayz4sci.boilerplate.ui.splash.SplashFragment

class FragmentSwitcher(var activity: Home, private var fragmentManager: FragmentManager) : FragmentSwitcherInterface {

    private fun addFullScreenFragment(fragment: BaseFragment, addToBackStackEntry: Boolean = true) {
        activity.hideFullScreenLoader()

        if (addToBackStackEntry) {
            fragmentManager.beginTransaction()
                .addToBackStack(fragment.tagText)
                .replace(R.id.home_full_screen_container, fragment, fragment.tagText)
                .commitAllowingStateLoss()
        } else {
            //Not adding to backstack so that when it's replaced it will be destroyed.
            fragmentManager.beginTransaction()
                .replace(R.id.home_full_screen_container, fragment)
                .commitAllowingStateLoss()
        }
    }

    override fun removeFragment(tag: String) {
        try {
            val fragment = fragmentManager.findFragmentByTag(tag)
            if (fragment != null) {
                fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss()
                fragmentManager.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        } catch (ignored: Exception) {
        }
    }

    override fun showSplashScreen() {
        addFullScreenFragment(SplashFragment(), false)
    }

    override fun showMainFragment() {
        addFullScreenFragment(MainFragment())
    }
}