package com.example.axrorxoja.feedlist.storage.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Post(var name: String = "",
                var author: String = "",
                var publishedAt: Long = System.currentTimeMillis()) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0

}
