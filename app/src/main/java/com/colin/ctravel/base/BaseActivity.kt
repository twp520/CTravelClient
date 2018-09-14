package com.colin.ctravel.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.colin.ctravel.R
import com.colin.ctravel.net.ApiException
import kotlinx.android.synthetic.main.base_toolbar.*
import retrofit2.HttpException

/**
 *create by colin 2018/9/12
 */
abstract class BaseActivity<P : BasePresenter> : AppCompatActivity(), BaseView {

    var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setContentViewId())
        mPresenter = createPresenter()
        setSupportActionBar(base_toolbar)
        initView()
    }

    fun setToolbarTitle(titleRes: Int) {
        supportActionBar?.setTitle(titleRes)
    }

    fun setNavClick() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        base_toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun addToolbarMenu(@MenuRes menuId: Int) {
        base_toolbar.inflateMenu(menuId)
        base_toolbar.setOnMenuItemClickListener {
            onMenuClick(it)
            return@setOnMenuItemClickListener true
        }
    }

    protected open fun onMenuClick(menuItem: MenuItem) {

    }

    @LayoutRes
    abstract fun setContentViewId(): Int

    abstract fun createPresenter(): P?

    override fun showLoading() {
        //TODO not
    }

    override fun dismissLoading() {
        //TODO not
    }

    override fun showTipMessage(msg: String) {
//        val view = layoutInflater.inflate(setContentViewId(),null,false)
//        Snackbar.make(window.decorView, msg, Snackbar.LENGTH_SHORT).show()
        val snackbar = Snackbar.make(getSnackbarView(), msg, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        snackbar.show()
    }

    open fun getSnackbarView(): View {
        return window.decorView.rootView.findViewById(android.R.id.content)
    }

    override fun getViewContext(): Context {
        return this
    }

    override fun getNetKey(): String {
        return this::javaClass.name
    }

    override fun showNetErrorMsg(throwable: Throwable) {
        //进行错误显示
        dismissLoading()
        when (throwable) {
            is ApiException -> showTipMessage(throwable.msg ?: "")
            is HttpException -> showTipMessage("http:" + throwable.message)
            else -> {
                showTipMessage("some thing:" + throwable.message)
                throwable.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDettach()
        mPresenter = null
    }
}