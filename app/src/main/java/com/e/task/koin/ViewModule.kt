package com.e.task.koin

import android.app.Application
import com.e.task.utils.ViewModelProviderFactory
import org.koin.dsl.module

val viewModule = module {
    single { provideViewModelProviderFactory(get()) }
}

private fun provideViewModelProviderFactory(application: Application): ViewModelProviderFactory {
    return ViewModelProviderFactory(application)
}