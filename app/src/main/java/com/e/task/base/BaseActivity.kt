package com.e.task.base

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.e.task.utils.RequestPermission


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseNavigator {

    private var viewDataBinding: T? = null
    private var mViewModel: V? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V?

    abstract val isFullScreen: Boolean

    abstract val isStatusBar: Boolean

    var requestPermission: RequestPermission? = null

    fun getViewDataBinding(): T {
        return viewDataBinding!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isFullScreen) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        super.onCreate(savedInstanceState)
        performDataBinding()
        initRequestPermission()
    }
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (null != requestPermission && requestCode == RequestPermission.REQUEST_CODE_PERMISSION)
            requestPermission!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun initRequestPermission() {
        if (null == requestPermission)
            requestPermission = RequestPermission(this, false)
    }
    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }


    override fun onInternetConnectionError() {

    }
    override fun onDestroy() {
        super.onDestroy()
    }

}