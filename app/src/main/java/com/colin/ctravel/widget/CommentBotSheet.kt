package com.colin.ctravel.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.colin.ctravel.R
import com.colin.ctravel.adapter.CommentAdapter
import com.colin.ctravel.bean.Comment

class CommentBotSheet : BottomSheetDialogFragment() {

    private var mListener: ((content: String) -> Unit)? = null
    private var mList: RecyclerView? = null
    private var mAdapter: CommentAdapter? = null
    private var mTempData = arrayListOf<Comment>()
    private var mEdit: EditText? = null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        if (args != null)
            mTempData.addAll(args.getParcelableArrayList("data"))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.bot_comment, container, false)
        //设置评论信息
        mEdit = mView.findViewById(R.id.bot_comment_edit)
        val btn = mView.findViewById<ImageButton>(R.id.bot_comment_btn_send)
        mList = mView.findViewById(R.id.bot_comment_list)
        mAdapter = CommentAdapter(mTempData)
        mAdapter?.addHeaderView(inflater.inflate(R.layout.bot_comment_list_head, mList, false))
        mList?.adapter = mAdapter
        mList?.itemAnimator = DefaultItemAnimator()
        mEdit?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btn.isEnabled = s?.isNotBlank() == true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        btn.setOnClickListener {
            mListener?.invoke(mEdit?.text?.toString() ?: "")
        }
        return mView
    }

    fun setCommentListener(listener: (content: String) -> Unit) {
        mListener = listener
    }

    fun setData(data: MutableList<Comment>) {
        mAdapter?.replaceData(data)
    }

    fun addData(comment: Comment) {
        mAdapter?.addData(comment)
        mList?.smoothScrollToPosition((mAdapter?.itemCount ?: 1-1) ?: 0)
        mEdit?.setText("")
    }

}