package com.example.axrorxoja.feedlist.storage.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface IPostDAO {
    @Insert fun addPost(post: List<Post>)

    @Query("SELECT * from post ORDER BY id DESC") fun loadAll(): Single<List<Post>> //todo for testing

    @Query("SELECT * from post WHERE id<=:id ORDER BY id DESC limit :limit")
    fun loadOlderPostsById(id: Long, limit: Int = 10): Single<List<Post>>

    @Query("SELECT * from post ORDER BY id DESC limit :limit")
    fun loadPart(limit: Int = 10): Single<List<Post>>

    @Query("delete  from post")
    fun deleteAll()
}