package com.ayz4sci.boilerplate.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by Maxim Bircu
 *
 * This is similar to SingleLiveEvent
 * Works as regular live data. You can subscribe using 2 methods:
 * observe -> will notify every time
 * observeSingleEvent -> will notify for each new event
 *
 * Note: observeSingleEvent will only notify one observer.
 */
class CustomLiveData<T> : MutableLiveData<T>() {
    private var lastHandledItem: T? = null

    fun observeSingleEvent(owner: LifecycleOwner, observer: (T) -> Unit) {
        this.observe(owner, Observer {
            if (it != lastHandledItem) {
                observer(it)
                lastHandledItem = it
            }
        })
    }
}