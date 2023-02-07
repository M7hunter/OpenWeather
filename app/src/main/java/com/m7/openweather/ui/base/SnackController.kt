package com.m7.openweather.ui.base

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.m7.openweather.R

class SnackController(private val parentView: View) {

    private fun snackCallback(onDismissed: (() -> Unit)) = object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDismissed.invoke()
        }
    }

    fun displayMessage(msg: String, onDismissed: (() -> Unit)? = null) {
        Snackbar.make(parentView, msg, Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(parentView.context.getColor(R.color.green_200))
            setTextColor(Color.WHITE)
        }.addCallback(snackCallback { onDismissed?.invoke() }).show()
    }

    fun displayError(msg: String, onDismissed: (() -> Unit)? = null) {
        Snackbar.make(parentView, msg, Snackbar.LENGTH_SHORT).apply {
            setBackgroundTint(Color.RED)
            setTextColor(Color.WHITE)
        }.addCallback(snackCallback { onDismissed?.invoke() }).show()
    }
}