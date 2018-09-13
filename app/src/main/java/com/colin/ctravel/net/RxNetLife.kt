package com.colin.ctravel.net

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Created by tianweiping on 2017/12/11.
 */

object RxNetLife {
    private val disposableHashMap: HashMap<String, CompositeDisposable> = HashMap()

    fun add(key: String?, disposable: Disposable?) {
        if (disposable == null || key == null)
            return
        var compositeDisposable: CompositeDisposable? = disposableHashMap[key]
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
            disposableHashMap[key] = compositeDisposable
        }
        compositeDisposable.add(disposable)
    }

    fun clear(key: String?) {
        if (key == null)
            return
        val compositeDisposable = disposableHashMap[key]
        compositeDisposable?.clear()
    }


}
