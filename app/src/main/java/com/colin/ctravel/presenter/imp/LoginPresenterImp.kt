package com.colin.ctravel.presenter.imp

import com.colin.ctravel.base.BasePresenterImp
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.net.RxNetLife
import com.colin.ctravel.presenter.LoginPresenter
import com.colin.ctravel.view.LoginView

/**
 *create by colin 2018/9/12
 */
class LoginPresenterImp(view: LoginView) : BasePresenterImp<LoginView>(view), LoginPresenter {

    override fun login(account: String, pwd: String) {
        if (account.isBlank() || pwd.isBlank()) {
            return
        }
        view?.showLoading()
        val disposable = TravelModule.login(account, pwd)
                .subscribe({
                    //保存用户信息
                    TravelModule.saveUser(it,view!!.getViewContext())
                    view?.dismissLoading()
                    view?.loginSuccess()
                }, {
                    view?.dismissLoading()
                    view?.showNetErrorMsg(it)
                })
        RxNetLife.add(view?.getNetKey(), disposable)
    }


}