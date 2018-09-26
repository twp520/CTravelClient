package com.colin.ctravel.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.colin.ctravel.R
import com.colin.ctravel.activity.PostDetailAct
import com.colin.ctravel.activity.SendPostActivity
import com.colin.ctravel.adapter.PostAdapter
import com.colin.ctravel.base.BaseFragment
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.util.jumpActivity
import com.scwang.smartrefresh.header.TaurusHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *create by colin 2018/9/26
 */
class PostFragment : BaseFragment() {

    private var mAdapter: PostAdapter? = null
    private var mRefresh: SmartRefreshLayout? = null
    private var mRecycler: RecyclerView? = null
    private var mFab: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        mRefresh = view.findViewById(R.id.post_refresh)
        mRecycler = view.findViewById(R.id.post_recycler)
        mFab = view.findViewById(R.id.post_fab)
        initRecyclerView()
        return view
    }


    private fun initRecyclerView() {
        val header = TaurusHeader(activity)
        header.setBackgroundColor(ContextCompat.getColor(getViewContext(), R.color.colorPrimary))
        mRefresh?.setRefreshHeader(header)
        mAdapter = PostAdapter(mutableListOf())
        mAdapter?.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
        mRecycler?.adapter = mAdapter
        mFab?.isEnabled = true
        mFab?.setOnClickListener {
            jumpActivity(SendPostActivity::class.java)
        }
        mRefresh?.autoRefresh(300)
        mRefresh?.setOnRefreshListener {
            //刷新数据
            loadData(0)
        }
        mAdapter?.setOnItemClickListener { _, itemView, position ->
            //跳转到详情页面
            val temp = mAdapter?.data?.get(position)
            if (temp != null) {
                val shareView = itemView.findViewById<ImageView>(R.id.item_post_photo)
                gotoDetail(shareView, temp)
            }
        }
    }

    private fun refreshList(data: MutableList<PostInfo>) {
        mAdapter?.replaceData(data)
        mRefresh?.finishRefresh(1000, true)
    }

    private fun refreshFail() {
        mRefresh?.finishRefresh(false)
    }

    override fun getSnackbarView(): View {
        return mRecycler ?: TextView(activity)
    }

    private fun loadData(page: Int) {
        TravelModule.getAllPost()
                .subscribe({
                    refreshList(it)
                }, {
                    refreshFail()
                    showNetErrorMsg(it)
                    it.printStackTrace()
                })
    }

    private fun gotoDetail(shareView: View, temp: PostInfo) {
        if (activity == null)
            return
        val bundle = Bundle()
        bundle.putParcelable("post", temp)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                shareView,
                getString(R.string.t_post_list_to_detail))
        jumpActivity(PostDetailAct::class.java, bundle, optionsCompat.toBundle())
    }
}