package com.example.axrorxoja.feedlist

import android.app.Application
import android.arch.persistence.room.Room
import android.preference.PreferenceManager
import com.example.axrorxoja.feedlist.storage.db.AppDataBase
import com.example.axrorxoja.feedlist.storage.pref.IPreference
import com.example.axrorxoja.feedlist.storage.pref.PreferenceImpl

class App : Application() {
    val db: AppDataBase by lazy { initDB() }
    val pref: IPreference by lazy { initPref() }

    private fun initPref(): PreferenceImpl {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        return PreferenceImpl(preferences)
    }

    private fun initDB(): AppDataBase {
        return Room.databaseBuilder(this, AppDataBase::class.java, "database")
                .build()
    }
}
