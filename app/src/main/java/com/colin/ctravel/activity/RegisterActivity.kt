package com.colin.ctravel.activity

import android.app.Activity
import android.app.AlertDialog
import android.view.WindowManager
import com.colin.ctravel.R
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.base.BasePresenter
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.net.RxNetLife
import com.colin.ctravel.util.LOG_TAG
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity<BasePresenter>() {

    override fun beforeSetContentView() {
        //请求沉浸式
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun setContentViewId(): Int {
        return R.layout.activity_register
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun initView() {
//        setNavClick()
        register_btn?.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val account = register_edit_account.text.toString()
        if (account.isBlank()) {
            register_input_account.error = "请输入电话号码"
            return
        }

        val nickname = register_edit_nickname.text.toString()
        if (nickname.isBlank()) {
            register_input_nickname.error = "请输入昵称"
            return
        }

        val pwd = register_edit_pwd.text.toString()
        if (pwd.isBlank()) {
            register_input_account.error = "请输入密码"
            return
        }
        var gender = 0
        //选择性别
        val dialog = AlertDialog.Builder(this)
                .setTitle("Choice your gender")
                .setSingleChoiceItems(R.array.array_gender, 0) { _, which ->
                    KLog.e(LOG_TAG, "which = $which")
                    gender = which
                }
                .setPositiveButton("选好了") { _, _ ->
                    //进行注册
                    register(account, nickname, pwd, gender)
                }
                .create()
        dialog.show()

    }

    private fun register(account: String, nickname: String, pwd: String, gender: Int) {
        val disposable = TravelModule.register(account, pwd, nickname, gender)
                .flatMap {
                    TravelModule.saveUser(it, getViewContext())
                }
                .subscribe({
                    dismissLoading()
                    //注册成功
                    KLog.e(LOG_TAG, "注册成功")
                    setResult(Activity.RESULT_OK)
                    finish()
                }, {
                    it.printStackTrace()
                    showNetErrorMsg(it)
                    it.printStackTrace()
                })
        RxNetLife.add(getNetKey(), disposable)
    }


}
