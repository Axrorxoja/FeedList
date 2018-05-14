package com.example.axrorxoja.feedlist.ui.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.axrorxoja.feedlist.R
import com.example.axrorxoja.feedlist.storage.db.Post
import com.example.axrorxoja.feedlist.ui.adapter.callbacks.PostCallBack
import kotlinx.android.synthetic.main.row_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(PostCallBack()) {
    private val formatter: SimpleDateFormat by lazy { SimpleDateFormat("MMM dd,yyyy HH:mm", Locale.getDefault()) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemId(position: Int) = if (itemCount == 0) 0 else getItem(position).id

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) = holder.onBind(getItem(position))

    override fun submitList(list: List<Post>) {
        super.submitList(list)
        Log.d("myTag", "list size ${list.size}")
    }

    inner class PostViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun onBind(it: Post) {
            view.tvName.text = it.name
            view.tvAuthor.text = it.author
            view.tvDate.text = formatter.format(Date(it.publishedAt))
        }
    }
}
