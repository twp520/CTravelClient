package com.colin.ctravel.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.WindowManager
import com.colin.ctravel.R
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.presenter.LoginPresenter
import com.colin.ctravel.presenter.imp.LoginPresenterImp
import com.colin.ctravel.util.jumpActivity
import com.colin.ctravel.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginPresenter>(), LoginView {

    override fun beforeSetContentView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun setContentViewId(): Int {
        return R.layout.activity_login
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenterImp(this)
    }

    override fun initView() {
        supportActionBar?.title = getString(R.string.login_title)
        login_btn_account?.setOnClickListener { _ ->
            //进行登陆检查
            login()
        }
        login_edit_account?.setOnFocusChangeListener { _, _ ->
            login_input_account?.error = ""
        }
        login_edit_pwd?.setOnFocusChangeListener { _, _ ->
            login_input_pwd?.error = ""
        }

        val builder = SpannableStringBuilder(getString(R.string.login_no_account))
        builder.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)),
                5, builder.length, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        login_btn_register.text = builder
        login_btn_register.setOnClickListener {
            //TODO 跳转到注册页面
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, 200)
        }
    }

    private fun login() {
        val account = login_edit_account?.text.toString()
        val pwd = login_edit_pwd?.text.toString()
        if (account.isBlank()) {
            login_input_account?.error = "用户名为空"
            return
        }
        if (pwd.isBlank()) {
            login_input_pwd?.error = "密码为空"
            return
        }
        //登陆
        mPresenter?.login(account, pwd)
    }

    override fun loginSuccess() {
        jumpActivity(MainActivity::class.java, null)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            loginSuccess()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
