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
        val user = TravelModule.getUser()
        user?.let {
            view?.initUser(it)
        }
    }

    override fun loadData(page: Int) {
        view?.showLoading()
        val tempData = mutableListOf<PostInfo>()
        for (i in 1..10) {
            tempData.add(PostInfo(i))
        }
        TravelModule.getAllPost()
                .subscribe({
                    view?.dismissLoading()
                    it.addAll(tempData)
                    view?.refreshList(it)

                }, {
                    view?.dismissLoading()
                    view?.showNetErrorMsg(it)
                })
    }
}