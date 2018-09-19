package com.colin.ctravel.activity

import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_BOTTOM
import com.colin.ctravel.R
import com.colin.ctravel.adapter.PostAdapter
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.bean.User
import com.colin.ctravel.presenter.MainPresenter
import com.colin.ctravel.presenter.imp.MainPresenterImp
import com.colin.ctravel.util.GlideApp
import com.colin.ctravel.util.jumpActivity
import com.colin.ctravel.view.MainView
import com.scwang.smartrefresh.header.TaurusHeader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base_toolbar.*

class MainActivity : BaseActivity<MainPresenter>(), MainView {

    private var mAdapter: PostAdapter? = null

    override fun setContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenterImp(this)
    }

    override fun initView() {
        //设置左边按钮

        initActionBar()
        initRecyclerView()
        mPresenter?.init()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val toggle = ActionBarDrawerToggle(this, main_drawer, base_toolbar, R.string.open, R.string.close)
        toggle.syncState()
        main_drawer.addDrawerListener(toggle)
    }

    private fun initRecyclerView() {
        val header = TaurusHeader(this)
        header.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        main_refresh.setRefreshHeader(header)
        mAdapter = PostAdapter(mutableListOf())
        mAdapter?.openLoadAnimation(SLIDEIN_BOTTOM)
        main_recycler.adapter = mAdapter
        main_fab.isEnabled = true
        main_fab.setOnClickListener {
            jumpActivity(SendPostActivity::class.java)
        }
        main_refresh.autoRefresh(300)
        main_refresh.setOnRefreshListener {
            //刷新数据
            mPresenter?.loadData(1)
        }
        mAdapter?.setOnItemClickListener { _, itemView, position ->
            //TODO 跳转到详情页面
            val temp = mAdapter?.data?.get(position)
            if (temp != null) {
                val shareView = itemView.findViewById<ImageView>(R.id.item_post_photo)
                gotoDetail(shareView, temp)
            }
        }
    }

    private fun gotoDetail(shareView: View, temp: PostInfo) {
        val bundle = Bundle()
        bundle.putParcelable("post", temp)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                shareView,
                getString(R.string.t_post_list_to_detail))
        jumpActivity(PostDetailAct::class.java, bundle)
    }

    override fun initUser(user: User) {
        val icon = main_nav.getHeaderView(0).findViewById<ImageView>(R.id.nav_head_icon)
        val nickname = main_nav.getHeaderView(0).findViewById<TextView>(R.id.nav_head_name)
        val account = main_nav.getHeaderView(0).findViewById<TextView>(R.id.nav_head_account)
        account.text = user.account
        nickname.text = user.nickname
        val d = when (user.gender) {
            0 -> getDrawable(R.drawable.gender_male)
            1 -> getDrawable(R.drawable.gender_female)
            2 -> getDrawable(R.drawable.icon_et)
            else -> getDrawable(R.drawable.icon_et)
        }
        nickname.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)
        if (user.fromWx) {
            GlideApp.with(this).load(user.headUrl).into(icon)
        } else {
            //TODO 设置默认头像
            if (user.headUrl.isBlank()) {
                GlideApp.with(this).load(R.drawable.logo).into(icon)
            } else {
                GlideApp.with(this).load(user.headUrl).into(icon)
            }
        }
    }

    override fun refreshList(data: MutableList<PostInfo>) {
        mAdapter?.replaceData(data)
        main_refresh.finishRefresh(1000, true)
    }

    override fun refreshFail() {
        main_refresh.finishRefresh(false)
    }

    override fun getSnackbarView(): View {
        return main_recycler
    }

    override fun onBackPressed() {
        if (main_drawer.isDrawerOpen(Gravity.START)) {
            main_drawer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }
}
