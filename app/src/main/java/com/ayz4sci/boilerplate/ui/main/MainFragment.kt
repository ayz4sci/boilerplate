package com.ayz4sci.boilerplate.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ayz4sci.boilerplate.R
import com.ayz4sci.boilerplate.ui.common.BaseFragment
import com.crashlytics.android.Crashlytics

class MainFragment : BaseFragment() {

    companion object {
        const val TAG = "mainFragment"
    }

    override val tagText: String
        get() = TAG

    private lateinit var viewModel: MainViewModel

    override fun onBackPressed(): Boolean {
        activity.finish()
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

    }

}
