package com.colin.ctravel.presenter.imp

import android.os.Bundle
import com.colin.ctravel.base.BasePresenterImp
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.net.RxNetLife
import com.colin.ctravel.presenter.PostDetailPresenter
import com.colin.ctravel.util.LOG_TAG
import com.colin.ctravel.view.PostDetailView
import com.socks.library.KLog

/**
 *create by colin 2018/9/27
 */
class PostDetailPresenterImp(view: PostDetailView) : BasePresenterImp<PostDetailView>(view), PostDetailPresenter {

    private var postId = -1

    override fun onAttach(bundle: Bundle) {
        postId = bundle.getInt("pid", -1)
    }

    override fun loadCommentData() {
        val disposable = TravelModule.getPostComment(postId)
                .subscribe({
                    view?.setCommentData(it)
                }, { throwable ->
                    view?.showNetErrorMsg(throwable)
                })
        RxNetLife.add(view?.getNetKey(), disposable)
    }

    override fun favoritePost() {
        if (postId == -1) {
            view?.showTipMessage("暂时不可用~")
            return
        }
        view?.showLoading()
        val disposable = TravelModule.favoritePost(postId)
                .subscribe({
                    //收藏成功
                    view?.dismissLoading()
                    view?.favSuccess()
                }, {
                    view?.dismissLoading()
                    view?.showNetErrorMsg(it)
                })
        RxNetLife.add(view?.getNetKey(), disposable)
    }

    override fun sendComment(content: String) {
        val disposable = TravelModule.sendComment(content, postId)
                .subscribe({
                    view?.commentSuccess(it)
                    KLog.e(LOG_TAG, it.toString())
                }, {
                    view?.commentFail()
                    view?.showNetErrorMsg(it)
                })
        RxNetLife.add(view?.getNetKey(), disposable)
    }
}