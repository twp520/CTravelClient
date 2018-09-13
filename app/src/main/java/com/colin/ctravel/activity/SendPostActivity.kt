package com.colin.ctravel.activity

import com.colin.ctravel.R
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.base.BasePresenter

class SendPostActivity : BaseActivity<BasePresenter>() {

    override fun setContentViewId(): Int {
        return R.layout.activity_send_post
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun initView() {

    }

}
