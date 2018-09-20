package com.colin.ctravel.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.colin.ctravel.R

class CommentBotSheet : BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.bot_comment, container, false)
        //todo 设置评论信息
        val edit = mView.findViewById<EditText>(R.id.bot_comment_edit)
        val btn = mView.findViewById<ImageButton>(R.id.bot_comment_btn_send)
        val list = mView.findViewById<RecyclerView>(R.id.bot_comment_list)
        val temp = mutableListOf<String>()
        for (i in 1..50) {
            temp.add("评论$i")
        }
        list.adapter = MyAdapter(temp)
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btn.isEnabled = s?.isNotEmpty() == true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        return mView
    }


    private class MyAdapter(data: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(data) {
        init {
            mLayoutResId = R.layout.item_comment
        }

        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.item_comment_tv, item)
        }
    }


}