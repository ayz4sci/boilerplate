package com.ayz4sci.boilerplate.di

import android.annotation.SuppressLint
import com.ayz4sci.boilerplate.ui.viewHelpers.common.SharedPreferencesHelper
import com.ayz4sci.boilerplate.data.repository.ApiRepositoryImpl
import com.ayz4sci.boilerplate.data.repository.base.ApiRepository
import com.ayz4sci.boilerplate.data.repository.base.HttpInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

@SuppressLint("SyntheticAccessor")
        /**
         * Created by @ayz4sci on 16/08/2018.
         */
val appModule = module {
    single { SharedPreferencesHelper(androidContext()) }
    single { HttpInterceptor(get()) }
    single { ApiRepositoryImpl() as ApiRepository }
}