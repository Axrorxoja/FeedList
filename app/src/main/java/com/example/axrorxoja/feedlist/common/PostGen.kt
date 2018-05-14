package com.example.axrorxoja.feedlist.common

import android.util.Log
import com.example.axrorxoja.feedlist.storage.db.Post

class PostGen {
    private var c = 0

    fun loadPosts(): List<Post> {
        val list = mutableListOf<Post>()
        (c..(c + 4)).forEach {
            list.add(Post("name-$it",
                    "author-$it",
                    System.currentTimeMillis() - 1000 * 3600 * it))
        }
        c += 5
        Log.d("mytag", "c=$c")
        return list
    }
}