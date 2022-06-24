package com.e.task.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(
    application: Application
) : AndroidViewModel(application) {

    private lateinit var navigator: WeakReference<N>

    fun getNavigator(): N? {
        return navigator.get()
    }

    fun isInitialized(): Boolean {
        return ::navigator.isInitialized
    }

    fun setNavigator(navigator: N) {
        this.navigator = WeakReference(navigator)
    }

}