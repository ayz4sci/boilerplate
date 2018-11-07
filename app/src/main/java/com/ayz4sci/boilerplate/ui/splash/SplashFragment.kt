package com.ayz4sci.boilerplate.ui.splash

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayz4sci.boilerplate.R
import com.ayz4sci.boilerplate.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : BaseFragment() {
    companion object {
        const val TAG = "splashFragment"
    }

    override val tagText: String
        get() = TAG

    override fun onBackPressed(): Boolean {
        activity.finish()
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_splash.animate().alpha(1f)
            .setDuration(2500)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    showNextScreen()
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }
            })
    }

    fun showNextScreen() {
        activity.fragmentSwitcher.showMainFragment()
    }
}
