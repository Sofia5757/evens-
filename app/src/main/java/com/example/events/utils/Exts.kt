package com.example.events.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext

fun Context.showAlert(message: String = "Непредвиденная ошибка"){
    AlertDialog.Builder(this)
        .setMessage(message)
        .show()
}