package com.colin.ctravel.activity

import com.colin.ctravel.R
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.base.BasePresenter

class PostDetailAct : BaseActivity<BasePresenter>() {
    override fun setContentViewId(): Int {
        return R.layout.activity_post_detail
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    override fun initView() {

    }

}
