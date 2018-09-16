package com.colin.ctravel.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.nio.file.Files.exists
import java.nio.file.Files.isDirectory


const val LOG_TAG = "travel"

val photoCompressDirPath = Environment.getExternalStorageDirectory().absolutePath + "/CTravelCache"

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.jumpActivity(clazz: Class<*>, args: Bundle? = null) {
    val intent = Intent(this, clazz)
    args?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}


fun createOrExistsDir(file: File?): Boolean {
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

fun createOrExistsFile(file: File?): Boolean {
    if (file == null) return false
    if (file.exists()) return file.isFile()
    if (!createOrExistsDir(file.parentFile)) return false
    return try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }

}
