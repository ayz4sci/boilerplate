package com.ayz4sci.boilerplate.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    lateinit var activity: Home


    /**
     * Will be used as handle to save transactions in backstack
     *
     * @return tag text
     */
    abstract val tagText: String

    /**
     * To enable fragments capture back-press event and utilize it before
     * it's used in the hosting Activity.
     *
     * @return true if consumed, else false
     */
    open fun onBackPressed(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = getActivity() as Home

        setHasOptionsMenu(true)
    }

}