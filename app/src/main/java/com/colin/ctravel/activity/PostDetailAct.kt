package com.colin.ctravel.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.colin.ctravel.R
import com.colin.ctravel.adapter.CommentAdapter
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.base.BasePresenter
import com.colin.ctravel.bean.Comment
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.net.RxNetLife
import com.colin.ctravel.util.GlideApp
import com.colin.ctravel.util.LOG_TAG
import com.colin.ctravel.util.TimeUtils
import com.colin.ctravel.widget.CommentBotSheet
import com.socks.library.KLog
import kotlinx.android.synthetic.main.activity_post_detail.*
import org.json.JSONArray
import java.text.SimpleDateFormat

class PostDetailAct : BaseActivity<BasePresenter>() {

    private var commentBot: CommentBotSheet? = null
    private var fourAdapter: CommentAdapter? = null

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
            //下载评论数据
            val disposable = TravelModule.getPostComment(post.id)
                    .subscribe({ commentList ->
                        if (commentList != null) {
                            //全部评论设置到 bot中
                            commentBot = CommentBotSheet()
                            val args = Bundle()
                            args.putParcelableArrayList("data", ArrayList<Comment>(commentList))
                            commentBot?.arguments = args
                            //取4条设置到这里
                            fourAdapter = CommentAdapter(if (commentList.size > 4) commentList.subList(commentList.size - 4, commentList.size) else commentList)
                            detail_comment_ten.adapter = fourAdapter
                            initEvent(post)
                        }
                    }, { throwable ->
                        showNetErrorMsg(throwable)
                    })
            RxNetLife.add(getNetKey(), disposable)
        }

    }

    private fun initEvent(post: PostInfo) {
        detail_comment_btn.setOnClickListener {
            //弹出底部框
            commentBot?.show(supportFragmentManager, "commentBot")
        }

        commentBot?.setCommentListener { content ->
            KLog.e(LOG_TAG, "点击发送！！！")
            val disposable = TravelModule.sendComment(content, post.id)
                    .subscribe({
                        //发送成功
                        commentBot?.addData(it)
                        fourAdapter?.addData(it)
                        KLog.e(LOG_TAG, it.toString())
                    }, {
                        commentBot?.dismiss()
                        showNetErrorMsg(it)
                    })
            RxNetLife.add(getNetKey(), disposable)
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
