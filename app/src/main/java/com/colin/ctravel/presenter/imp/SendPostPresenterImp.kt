package com.colin.ctravel.presenter.imp

import android.annotation.SuppressLint
import com.colin.ctravel.base.BasePresenterImp
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.presenter.SendPostPresenter
import com.colin.ctravel.util.TimeUtils
import com.colin.ctravel.view.SendPostView
import java.text.SimpleDateFormat

/**
 *create by colin 2018/9/14
 */
class SendPostPresenterImp(view: SendPostView) : BasePresenterImp<SendPostView>(view), SendPostPresenter {

    @SuppressLint("SimpleDateFormat")
    override fun sendPost() {
        if (view == null)
            return
        view?.showLoading()
        val map = hashMapOf<String, Any>()
        map["title"] = view?.getTitleText() ?: ""
        map["destination"] = view?.getDesText() ?: ""
        map["departure"] = view?.getDepText() ?: ""
        if (view?.getDateText() != null) {
            map["startTime"] = TimeUtils.string2Millis(view?.getDateText(), SimpleDateFormat("yyyy-mm-dd"))
        }
        map["userId"] = TravelModule.getUser()?.id ?: -1
        map["content"] = view?.getContentText() ?: ""
        val photos = view?.getPhotos()
        if (photos != null && photos.size > 0) {
            //上传图片
            TravelModule.upLoadPhoto(photos, view?.getViewContext())
                    .observe({ view!!.getLifecycle() }) {
                        if (it == null || it == "result_fail") {
                            view?.dismissLoading()
                            view?.showTipMessage("图片上传失败，请稍后再试")
                        } else {
                            map["imgs"] = it
                            sendPost(map)
                        }
                    }
        } else {
            sendPost(map)
        }
    }

    private fun sendPost(map: HashMap<String, Any>) {
        TravelModule.sendPost(map)
                .subscribe({
                    view?.dismissLoading()
                    view?.sendSuccess()
                }, {
                    view?.dismissLoading()
                    view?.showNetErrorMsg(it)
                })
    }
}