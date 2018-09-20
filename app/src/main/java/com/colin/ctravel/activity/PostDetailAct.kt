package com.colin.ctravel.activity

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.widget.NestedScrollView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.colin.ctravel.R
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.base.BasePresenter
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.util.GlideApp
import com.colin.ctravel.util.TimeUtils
import com.colin.ctravel.widget.CommentBotSheet
import kotlinx.android.synthetic.main.activity_post_detail.*
import org.json.JSONArray
import java.text.SimpleDateFormat

class PostDetailAct : BaseActivity<BasePresenter>() {
    override fun setContentViewId(): Int {
//        return R.layout.activity_post_detail_test
        return R.layout.activity_post_detail
    }

    override fun createPresenter(): BasePresenter? {
        return null
    }

    @SuppressLint("SimpleDateFormat")
    override fun initView() {
        postponeEnterTransition()
        if (intent == null) {
            showTipMessage("intent 为 null")
        }

        if (intent.extras == null) {
            showTipMessage("extras 为 null")
        }
        initActionBar()
        intent.extras?.let {
            val post = it.getParcelable<PostInfo>("post")
            detail_coll_tl.title = "行程详情"
            //图片
            /*val pageAdapter = MyPageAdapter(post.imgs, this)
            detail_vp.adapter = pageAdapter
            detail_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    KLog.e(LOG_TAG, "切换 position = $position")
                    KLog.e(LOG_TAG, "当前view的高度 = ${pageAdapter.getItemView(position).height}")
                }

            })*/
            if (post.imgs?.isNotEmpty() == true) {
                val jsonArray = JSONArray(post.imgs)
                GlideApp.with(this).load(jsonArray[0])
                        .into(detail_photos)
            } else {
                GlideApp.with(this).load(R.drawable.item_def)
                        .into(detail_photos)
            }
            val user = post.user
            if (user != null) {
                GlideApp.with(this).load(user.headUrl).into(detail_user_head)
                detail_user_nickname.text = user.nickname
            }
            //行程信息
            detail_info_title.text = post.title
            detail_info_content.text = post.content
            if (post.startTime != null) {
                detail_info_data.text = getString(R.string.detail_tv_startData,
                        TimeUtils.millis2String(post.startTime!!.toLong(), SimpleDateFormat("yyyy-mm-dd")))
            } else {
                detail_info_data.text = getString(R.string.detail_tv_startData, TimeUtils.getNowString())
            }
            detail_info_dep.text = getString(R.string.detail_tv_dep, post.departure)
            detail_info_des.text = getString(R.string.detail_tv_des, post.destination)
            startPostponedEnterTransition()
        }
        initEvent()
    }

    private fun initEvent() {
        detail_comment_btn.setOnClickListener {
            //TODO 弹出底部框
            val bot = CommentBotSheet()
            bot.show(supportFragmentManager, "commentBot")
        }

    }

    private fun initActionBar() {
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        detail_coll_tl.setContentScrimResource(R.color.colorPrimary)
        detail_toolbar.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_detail_share) {
            showTipMessage("分享还没想好")
        }
        return true
    }

    class MyPageAdapter(imgs: String?, var context: Context) : PagerAdapter() {

        private var mViews: MutableList<ImageView> = mutableListOf()
        private var urls: MutableList<String> = mutableListOf()

        init {
            if (imgs != null && imgs.isNotEmpty()) {
                //创建ImageView
                val jsonArray = JSONArray(imgs)
                for (i in 0 until jsonArray.length()) {
                    mViews.add(ImageView(context))
                    urls.add(jsonArray.optString(i))
                }
            }
        }

        override fun isViewFromObject(view: View, aobject: Any): Boolean {
            return view == aobject
        }

        override fun getCount(): Int {
            return mViews.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = mViews[position]
            val param = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.adjustViewBounds = true
            GlideApp.with(context).load(urls[position])
                    .optionalCenterCrop()
                    .into(imageView)
            imageView.layoutParams = param
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, aobj: Any) {
            container.removeViewAt(position)
        }

        fun getItemView(position: Int): View {
            return mViews[position]
        }

    }

}
