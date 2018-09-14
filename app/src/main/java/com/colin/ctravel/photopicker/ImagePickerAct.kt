package com.colin.ctravel.photopicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN
import com.colin.ctravel.R
import com.colin.ctravel.util.GalleryItemDecoration
import com.colin.ctravel.util.LOG_TAG
import com.colin.ctravel.util.jumpActivity
import com.socks.library.KLog
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_image_picke.*

class ImagePickerAct : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var mAdapter: ImageAdapter? = null
    private var spinnerAdapter: ArrayAdapter<Album>? = null
    private var maxChecked = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picke)
        maxChecked = intent.getIntExtra("maxCount", 9)
        initToolbar()
        initImageList()
        val scanner = ImageScanner(contentResolver)
        val permission = RxPermissions(this)
        permission.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { hasPermission ->
                    if (hasPermission) {
                        scanImage(scanner)
                    }
                }
        initEvent()
    }

    private fun initToolbar() {
        setSupportActionBar(picker_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        picker_toolbar.setNavigationOnClickListener {
            finish()
        }
        picker_tv_count.text = getString(R.string.picker_done, 0, maxChecked)
    }

    private fun initImageList() {
        mAdapter = ImageAdapter(mutableListOf())
        mAdapter?.openLoadAnimation(ALPHAIN)
        picker_list.adapter = mAdapter
        picker_list.addItemDecoration(GalleryItemDecoration(this, 2, 2, 2, 2))
    }

    private fun scanImage(scanner: ImageScanner) {
        scanner.getImageAlbum().observe({ lifecycle }, {
            it?.let { albumList: MutableList<Album> ->
                mAdapter?.setAllAlbum(albumList[0])
                initSpinner(albumList)
            }
        })
    }

    private fun initSpinner(albumList: MutableList<Album>) {
        spinnerAdapter = ArrayAdapter(this, R.layout.layout_spinner_withe, albumList)
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        picker_spinner.adapter = spinnerAdapter
        picker_spinner.onItemSelectedListener = this
    }

    private fun initEvent() {
        mAdapter?.setOnItemClickListener { _, _, position ->
            //TODO 跳转到大图页面查看大图
            val bundle = Bundle()
            bundle.putString("path", mAdapter!!.data[position].imagePath)
            jumpActivity(LargeActivity::class.java, bundle)
        }
        mAdapter?.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.item_picker_ckb -> {
                    if (maxChecked == 0) {
                        return@setOnItemChildClickListener
                    }
                    mAdapter!!.data[position].isChecked = !mAdapter!!.data[position].isChecked
                    mAdapter?.notifyItemChanged(position)
                    mAdapter?.checkedInAll(mAdapter!!.data[position])
                    //改变按钮上的数字加
                    picker_tv_count.text = getString(R.string.picker_done,
                            mAdapter?.getCheckedCount()
                                    ?: 0, maxChecked)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //没有任何选中
        KLog.d(LOG_TAG, "没有任何选中")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //切换相册源
        val album = spinnerAdapter?.getItem(position)
        mAdapter?.replaceData(album?.images ?: mutableListOf())
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_picker, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // 设置返回值，结束当前Activity
        val result = mAdapter?.getCheckedImage()
        if (result == null || result.isEmpty()) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return true
        }
        val intent = Intent()
        intent.putParcelableArrayListExtra("images", result)
        setResult(Activity.RESULT_OK, intent)
        finish()
        return true
    }

}
