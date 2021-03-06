package com.colin.ctravel.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.widget.Toast
import java.io.File
import java.io.IOException


const val LOG_TAG = "travel"

val photoCompressDirPath = Environment.getExternalStorageDirectory().absolutePath + "/CTravelCache"

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.jumpActivity(clazz: Class<*>, args: Bundle? = null, options: Bundle? = null) {
    val intent = Intent(this, clazz)
    args?.let {
        intent.putExtras(it)
    }
    if (options == null)
        startActivity(intent)
    else startActivity(intent, options)
}

fun Fragment.jumpActivity(clazz: Class<*>, args: Bundle? = null, options: Bundle? = null, requestCode: Int = -1) {
    val intent = Intent(activity, clazz)
    args?.let {
        intent.putExtras(it)
    }
    if (requestCode == -1) {
        if (options == null)
            startActivity(intent)
        else startActivity(intent, options)
    } else {
        if (options == null)
            startActivityForResult(intent, requestCode)
        else startActivityForResult(intent, requestCode, options)
    }
}

fun createOrExistsDir(file: File?): Boolean {
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

fun createOrExistsFile(file: File?): Boolean {
    if (file == null) return false
    if (file.exists()) return file.isFile
    if (!createOrExistsDir(file.parentFile)) return false
    return try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }

}

fun getUriFromFile(context: Context, file: File): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        FileProvider.getUriForFile(context, "com.colin.ctravel.FileProvider", file)
    else
        Uri.fromFile(file)
}
