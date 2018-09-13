package com.colin.ctravel.net

/**
 * create by colin
 */
data class ApiException(var code: Int, var msg: String?) : RuntimeException()
