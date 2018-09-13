package com.colin.ctravel.net


import com.colin.ctravel.base.BaseResultBean
import io.reactivex.functions.Function

/**
 * Created by colin
 */
class HandResultFunc<T> : Function<BaseResultBean<T>, T> {
    @Throws(Exception::class)
    override fun apply(t: BaseResultBean<T>): T? {
        if (t.code != NetConstant.RESULT_CODE_SUCCESS) {
            throw ApiException(t.code, t.msg)
        }
        return t.data
    }
}
