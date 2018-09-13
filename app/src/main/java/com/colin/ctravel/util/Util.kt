package com.colin.ctravel.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

const val LOG_TAG = "travel"

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.jumpActivity(clazz: Class<*>, args: Bundle?) {
    val intent = Intent(this, clazz)
    args?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}
