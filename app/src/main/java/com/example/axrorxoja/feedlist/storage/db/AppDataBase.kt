package com.example.axrorxoja.feedlist.storage.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Post::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun loadPostDAO(): IPostDAO
}
