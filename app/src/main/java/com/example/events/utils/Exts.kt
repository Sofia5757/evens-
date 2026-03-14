package com.example.events.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.events.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.showAlert(message: String? = "Непредвиденная ошибка"){
    AlertDialog.Builder(this)
        .setMessage(message)
        .show()
}

fun Date.toEventTime(): String{
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toEventDate(): String{
    val formatter = SimpleDateFormat("dd.MM", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toEventCreateDate(): String{
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toEventDetailsDate(): String{
    val formatter = SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault())
    return formatter.format(this)
}