package com.colin.ctravel.activity

import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.util.SparseArray
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import com.colin.ctravel.R
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.bean.User
import com.colin.ctravel.fragment.FavoriteFragment
import com.colin.ctravel.fragment.PostFragment
import com.colin.ctravel.presenter.MainPresenter
import com.colin.ctravel.presenter.imp.MainPresenterImp
import com.colin.ctravel.util.GlideApp
import com.colin.ctravel.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base_toolbar.*

class MainActivity : BaseActivity<MainPresenter>(), MainView {

    private var fragmentArray: SparseArray<Fragment>? = null

    override fun setContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenterImp(this)
    }

    override fun initView() {
        //设置左边按钮
        initActionBar()
        mPresenter?.init()
        fragmentArray = SparseArray()
        val postF = PostFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_fragment, postF, postF::class.java.simpleName).commit()
        fragmentArray?.put(R.id.menu_nav_post, postF)
        main_nav.setNavigationItemSelectedListener { item ->
            switchFragment(item.itemId)
            supportActionBar?.title = item.title
            true
        }

    }

    private fun initActionBar() {
        setToolbarTitle(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val toggle = ActionBarDrawerToggle(this, main_drawer, base_toolbar, R.string.open, R.string.close)
        toggle.syncState()
        main_drawer.addDrawerListener(toggle)
    }

    private fun switchFragment(id: Int) {
        var fragment = fragmentArray?.get(id)
        if (fragment == null) {
            fragment = when (id) {
                R.id.menu_nav_post -> {
                    PostFragment()
                }
                R.id.menu_nav_fav -> {
                    FavoriteFragment()
                }
                else -> PostFragment()
            }
            fragmentArray?.put(id, fragment)
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, fragment, fragment::class.java.simpleName)
                .commit()

        main_drawer.closeDrawers()
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
            else -> getDrawable(R.drawable.gender_male)
        }
        nickname.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)
        if (user.fromWx) {
            GlideApp.with(this)
                    .load(user.headUrl)
                    .optionalCircleCrop()
                    .into(icon)
        } else {
            //设置默认头像
            if (user.headUrl.isBlank()) {
                GlideApp.with(this).load(R.drawable.logo).into(icon)
            } else {
                GlideApp.with(this).load(user.headUrl).optionalCircleCrop().into(icon)
            }
        }
    }


    override fun onBackPressed() {
        if (main_drawer.isDrawerOpen(Gravity.START)) {
            main_drawer.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }
}
