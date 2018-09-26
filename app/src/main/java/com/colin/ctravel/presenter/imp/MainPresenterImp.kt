package com.colin.ctravel.presenter.imp

import com.colin.ctravel.base.BasePresenterImp
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.presenter.MainPresenter
import com.colin.ctravel.view.MainView

/**
 *create by colin 2018/9/13
 */
class MainPresenterImp(view: MainView) : BasePresenterImp<MainView>(view), MainPresenter {

    override fun init() {
        if (view == null)
            return
        TravelModule.getUser(view!!.getViewContext())
                ?.subscribe({ user ->
                    user?.let {
                        view?.initUser(it)
                    }
                }, {
                    view?.showNetErrorMsg(it)
                    it.printStackTrace()
                })
        view?.initPost()
    }


}