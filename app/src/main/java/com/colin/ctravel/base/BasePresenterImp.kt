package com.colin.ctravel.base

import android.os.Bundle
import com.colin.ctravel.net.RxNetLife

/**
 *create by colin 2018/9/12
 */
open class BasePresenterImp<V : BaseView>(var view: V?) : BasePresenter {


    override fun onAttach(bundle: Bundle) {

    }

    override fun onDettach() {
        RxNetLife.clear(view?.getNetKey())
        view = null
    }

}