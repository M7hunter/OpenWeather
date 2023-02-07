package com.m7.openweather.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.m7.openweather.data.model.CallResponse
import com.m7.openweather.data.model.CallState
import com.m7.openweather.data.source.local.SharedPrefManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    AppCompatActivity() {

    @Inject
    lateinit var sharedPrefs : SharedPrefManager

    lateinit var layoutBinding: B
    private lateinit var snackController: SnackController
    private val loadingDialog = LoadingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, layoutRes)
        snackController = SnackController(layoutBinding.root)
    }

    fun showMessage(msg: String, onDismissed: (() -> Unit)? = null) {
        snackController.displayMessage(msg, onDismissed)
    }

    fun showError(msg: String?, onDismissed: (() -> Unit)? = null) {
        snackController.displayError(msg ?: "error occurred", onDismissed)
    }

    fun displayLoading() {
        supportFragmentManager.executePendingTransactions()
        if (!loadingDialog.isAdded)
            loadingDialog.show(supportFragmentManager, loadingDialog.TAG)
    }

    fun dismissLoading() {
        if (loadingDialog.isAdded)
            loadingDialog.dismiss()
    }

    fun <T: CallResponse> observe(
        observable: StateFlow<CallState<T>?>,
        displayLoading: Boolean = true,
        showMessage: Boolean = false,
        onError: (() -> Unit)? = null,
        onSuccess: (data: T?) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                observable.collectLatest {
                    it?.apply {
                        when (status) {
                            CallState.Status.LOADING -> {
                                if (displayLoading) displayLoading()
                            }
                            CallState.Status.SUCCESS -> {
                                dismissLoading()
                                if (showMessage)
                                    msg?.also { showMessage(it) { onSuccess.invoke(data) } }
                                        ?: onSuccess.invoke(data)
                                else
                                    onSuccess.invoke(data)
                            }
                            CallState.Status.ERROR -> {
                                dismissLoading()
                                showError(ex?.message)
                                onError?.invoke()

                                ex?.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }
}