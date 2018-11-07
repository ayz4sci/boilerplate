package com.ayz4sci.boilerplate.di

import android.annotation.SuppressLint
import com.ayz4sci.boilerplate.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


@SuppressLint("SyntheticAccessor")
val viewModelModule = module {
    viewModel { MainViewModel() }
}
