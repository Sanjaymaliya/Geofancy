package com.e.task.ui.mainscreen

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import com.e.task.base.BaseViewModel

class MainViewModel(application: Application) : BaseViewModel<MainNavigator>(application) {

     var updateUIReciver: BroadcastReceiver? = null

     fun setBroadcastReceiver(context: Context)
     {
          val filter = IntentFilter()
          filter.addAction("service.to.activity.transfer")
          updateUIReciver = object : BroadcastReceiver() {
               override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent != null)
                         getNavigator()?.getData()
               }
          }
          context.registerReceiver(updateUIReciver, filter)
     }

}