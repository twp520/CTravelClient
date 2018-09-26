package com.colin.ctravel.base

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.colin.ctravel.R
import com.colin.ctravel.net.ApiException
import retrofit2.HttpException

/**
 *create by colin 2018/9/26
 */
abstract class BaseFragment : Fragment(), BaseView {

    override fun initView() {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun showTipMessage(msg: String) {
        val snackbar = Snackbar.make(getSnackbarView(), msg, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        snackbar.show()
    }

    abstract fun getSnackbarView(): View

    override fun getViewContext(): Context {
        return activity!!
    }

    override fun getNetKey(): String {
        return this::javaClass.name
    }

    override fun showNetErrorMsg(throwable: Throwable) {
        dismissLoading()
        when (throwable) {
            is ApiException -> showTipMessage(throwable.msg ?: "")
            is HttpException -> showTipMessage("http:" + throwable.message)
            else -> {
                showTipMessage("some thing:" + throwable.message)
                throwable.printStackTrace()
            }
        }
        throwable.printStackTrace()
    }
}