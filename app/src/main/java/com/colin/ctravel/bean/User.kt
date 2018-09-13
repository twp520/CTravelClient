package com.colin.ctravel.bean

/**
 *create by colin ${date}
 */
data class User(var id: Int = 0) {
    var account: String = ""
    var nickname: String = ""
    var gender: Int = 0
    var headUrl: String = ""
    var fromWx: Boolean = false
}