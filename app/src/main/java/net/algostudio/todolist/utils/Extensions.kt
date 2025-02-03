package net.algostudio.todolist.utils

import android.content.Context
import android.graphics.Color
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.google.android.material.R
import com.google.android.material.color.MaterialColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.toFormatDate(): String {
    val date = Date(this)
    if (DateUtils.isToday(this)) {
        val sdf = SimpleDateFormat("(dd MMMM yyyy)", Locale("id", "ID"))
        return "Today " + sdf.format(date)
    } else {
        val sdf = SimpleDateFormat("EEEE (dd MMMM yyyy)", Locale("id", "ID"))
        return sdf.format(date)
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableExt(key: String): T? = when {
    VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun TextView.setHTMLText(consent: String) {
    setText(
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            Html.fromHtml(consent, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(consent)
        }, TextView.BufferType.SPANNABLE
    )
    movementMethod = LinkMovementMethod.getInstance()
}

fun Context.getAttrColorPrimaryHex() = "#" + Integer.toHexString(
    MaterialColors.getColor(this, R.attr.colorPrimary, Color.WHITE)
).substring(2)

fun Calendar.getTodayDate(): Long{
    set(
        get(Calendar.YEAR),
        get(Calendar.MONTH),
        get(Calendar.DAY_OF_MONTH),
        0,
        0,
        0
    )
    set(Calendar.MILLISECOND, 0)

    return timeInMillis
}