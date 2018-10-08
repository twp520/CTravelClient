package com.colin.ctravel.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v7.graphics.Palette
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.colin.ctravel.R
import com.colin.ctravel.adapter.CommentAdapter
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.bean.Comment
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.presenter.PostDetailPresenter
import com.colin.ctravel.presenter.imp.PostDetailPresenterImp
import com.colin.ctravel.util.GlideApp
import com.colin.ctravel.util.ShareUtil
import com.colin.ctravel.util.TimeUtils
import com.colin.ctravel.view.PostDetailView
import com.colin.ctravel.widget.CommentBotSheet
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_post_detail.*
import org.json.JSONArray
import java.text.SimpleDateFormat

class PostDetailAct : BaseActivity<PostDetailPresenter>(), PostDetailView {

    private var commentBot: CommentBotSheet? = null
    private var fourAdapter: CommentAdapter? = null

    override fun beforeSetContentView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    override fun setContentViewId(): Int {
//        return R.layout.activity_post_detail_test
        return R.layout.activity_post_detail
    }

    override fun createPresenter(): PostDetailPresenter? {
        return PostDetailPresenterImp(this)
    }


    override fun initView() {
        postponeEnterTransition()
        initActionBar()
        intent.extras?.let {
            val post = it.getParcelable<PostInfo>("post")
            initDetailInfo(post)
            val initArgs = Bundle()
            initArgs.putInt("pid", post.id)
            mPresenter?.onAttach(initArgs)
            //下载评论数据
            mPresenter?.loadCommentData()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDetailInfo(post: PostInfo) {
        detail_coll_tl.title = "行程详情"
        //图片
        val pageAdapter = MyPageAdapter(post.imgs, this)
        detail_photo_vp.adapter = pageAdapter
        val user = post.user
        if (user != null) {
            GlideApp.with(this).load(user.headUrl)
                    .optionalCircleCrop()
                    .into(detail_user_head)
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


    private fun initActionBar() {
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        detail_coll_tl.setContentScrimResource(R.color.colorPrimary)
        var statusBarHeight1 = -1
        //获取status_bar_height资源的ID  
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight1 = resources.getDimensionPixelSize(resourceId)
        }
        val lp = detail_toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams
        lp.topMargin = statusBarHeight1
        detail_toolbar.layoutParams = lp
        detail_toolbar.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }
    }

    override fun setCommentData(commentList: MutableList<Comment>?) {
        if (commentList != null) {
            //全部评论设置到 bot中
            commentBot = CommentBotSheet()
            val args = Bundle()
            args.putParcelableArrayList("data", ArrayList<Comment>(commentList))
            commentBot?.arguments = args
            //取4条设置到这里
            fourAdapter = CommentAdapter(if (commentList.size > 4) commentList.subList(commentList.size - 4, commentList.size) else commentList)
            detail_comment_ten.adapter = fourAdapter
            initEvent()
        }
    }

    private fun initEvent() {
        detail_comment_btn.setOnClickListener {
            //弹出底部框
            commentBot?.show(supportFragmentManager, "commentBot")
        }

        commentBot?.setCommentListener { content ->
            if (content.isBlank()) {
                showTipMessage("不能发送空白消息哦")
                return@setCommentListener
            }
            mPresenter?.sendComment(content)
        }
    }

    private var likeMenu: MenuItem? = null

    override fun favSuccess() {
        //将图标变成红色
        likeMenu?.setIcon(R.drawable.icon_liked)
    }

    override fun commentSuccess(comment: Comment) {
        //发送成功
        commentBot?.addData(comment)
        fourAdapter?.addData(comment)
    }

    override fun commentFail() {
        commentBot?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post_detail, menu)
        likeMenu = menu?.findItem(R.id.menu_detail_like)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detail_share -> {
                //分享
                share()
            }
            R.id.menu_detail_like -> {
                mPresenter?.favoritePost()
            }
        }
        return true
    }

    private fun share() {
        val permission = RxPermissions(this)
        permission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) {
                        val post = intent.extras.getParcelable<PostInfo>("post")
                        showLoading()
                        val jsonArray = JSONArray(post.imgs)
                        GlideApp.with(this)
                                .asBitmap()
                                .load(jsonArray[0])
                                .into(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        ShareUtil.shareBitmap(this@PostDetailAct, resource, post.title
                                                ?: "", post.content
                                                ?: "")
                                                .subscribe({ intent ->
                                                    dismissLoading()
                                                    startActivity(intent)
                                                }, { th ->
                                                    dismissLoading()
                                                    th.printStackTrace()
                                                })
                                    }
                                })
                    }
                }
    }

    class MyPageAdapter(imgs: String?, var context: Activity) : PagerAdapter() {

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
            val target = object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)
                    Palette.from(resource).generate {
                        val lvs = it.lightVibrantSwatch
                        val dvs = it.darkVibrantSwatch
                        val dms = it.darkMutedSwatch
                        val lms = it.lightMutedSwatch
                        val color = when {
                            dvs?.rgb != null -> dvs.rgb
                            lvs?.rgb != null -> lvs.rgb
                            dms?.rgb != null -> dms.rgb
                            lms?.rgb != null -> lms.rgb
                            else -> ContextCompat.getColor(context, R.color.colorPrimary)
                        }
                        imageView.setBackgroundColor(color)
                    }
                }
            }
            GlideApp.with(context)
                    .asBitmap()
                    .load(urls[position])
                    .into(target)
            imageView.layoutParams = param
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, aobj: Any) {
            container.removeView(aobj as View)
        }

    }

}
