package com.example.axrorxoja.feedlist.ui.adapter.callbacks

import android.support.v7.util.DiffUtil
import com.example.axrorxoja.feedlist.storage.db.Post

class PostCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areContentsTheSame(oldItem: Post?, newItem: Post?) = oldItem == newItem

    override fun areItemsTheSame(oldItem: Post?, newItem: Post?) = oldItem?.id == newItem?.id
}