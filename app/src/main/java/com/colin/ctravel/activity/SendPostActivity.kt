package com.colin.ctravel.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.view.Menu
import android.view.MenuItem
import com.colin.ctravel.R
import com.colin.ctravel.adapter.PhotoAdapter
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.presenter.SendPostPresenter
import com.colin.ctravel.presenter.imp.SendPostPresenterImp
import com.colin.ctravel.util.GalleryItemDecoration
import com.colin.ctravel.view.SendPostView
import com.colin.picklib.Image
import com.colin.picklib.ImagePicker
import kotlinx.android.synthetic.main.activity_send_post.*
import java.text.SimpleDateFormat
import java.util.*

class SendPostActivity : BaseActivity<SendPostPresenter>(), SendPostView {


    private var mAdapter: PhotoAdapter? = null

    override fun setContentViewId(): Int {
        return R.layout.activity_send_post
    }

    override fun createPresenter(): SendPostPresenter {
        return SendPostPresenterImp(this)
    }

    override fun initView() {
        setToolbarTitle(R.string.post_title)
        setNavClick()
        initPhotoList()
        initDate()
    }

    private fun initPhotoList() {
        mAdapter = PhotoAdapter(mutableListOf())
        send_post_list.addItemDecoration(GalleryItemDecoration(this, 2, 2, 2, 2))
        send_post_list.itemAnimator = DefaultItemAnimator()
        send_post_list.adapter = mAdapter
        mAdapter?.setOnItemClickListener { _, _, position ->
            mAdapter?.remove(position)
        }
    }


    private fun initDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        send_post_date.text = getString(R.string.post_startDateS, "$year-${month + 1}-$day")
        val picker = DatePickerDialog(this, { _, pickYear, pickMonth, dayOfMonth ->
            send_post_date.text = getString(R.string.post_startDateS, "$pickYear-${pickMonth + 1}-$dayOfMonth")
        }, year, month, day)
        picker.datePicker.minDate = SimpleDateFormat.getDateInstance().calendar.time.time
        send_post_date.setOnClickListener {
            picker.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onMenuClick(item)
        return true
    }

    @SuppressLint("SimpleDateFormat")
    override fun onMenuClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_post_send -> {
                //发送按钮
                sendPost()
            }
            R.id.menu_post_add_photo -> {
                //添加图片
                if (mAdapter?.data?.size ?: 10 >= 9) {
                    return
                }
                ImagePicker.Builder().with(this)
                        .maxCount(9 - (mAdapter?.data?.size ?: 0))
                        .requestCode(200)
                        .needCrop(false)
                        .build()
                        .start()
            }
        }
    }

    private fun sendPost() {
        //进行检查
        if (getTitleText().isBlank()) {
            showTipMessage("请写一个标题吧~~")
            return
        }
        if (getDesText().isBlank()) {
            showTipMessage("请输入你们的目的地呀~~")
            return
        }
        if (getDepText().isBlank()) {
            showTipMessage("请输入起点呀~~")
            return
        }

        if (getContentText().isBlank()) {
            showTipMessage("介绍一下你的行程吧~~")
            return
        }
        mPresenter?.sendPost()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //接受回来的图片
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            val images = data.getParcelableArrayListExtra<Image>("images")
            if (mAdapter?.data?.size ?: 10 >= 9) {
                return
            }
            mAdapter?.addData(images)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getTitleText(): String {
        return send_post_edit_title.text.toString()
    }

    override fun getDesText(): String {
        return send_post_edit_des.text.toString()
    }

    override fun getDepText(): String {
        return send_post_edit_dep.text.toString()
    }

    override fun getDateText(): String {
        return send_post_date.text.toString().substring(5)
    }

    override fun getContentText(): String {
        return send_post_edit_content.text.toString()
    }

    override fun getPhotos(): MutableList<Image> {
        return mAdapter?.data ?: mutableListOf()
    }

    override fun sendSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
