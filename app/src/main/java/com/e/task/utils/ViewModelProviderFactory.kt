package com.e.task.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.e.task.ui.mainscreen.MainViewModel


class ViewModelProviderFactory(application: Application) :
    ViewModelProvider.Factory {

    private var application: Application = application

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {


            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                application
            ) as T



            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}