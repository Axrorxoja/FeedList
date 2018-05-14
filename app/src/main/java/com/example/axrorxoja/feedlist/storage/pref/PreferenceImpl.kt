package com.example.axrorxoja.feedlist.storage.pref

import android.content.SharedPreferences
import com.example.axrorxoja.feedlist.storage.pref.PreferenceImpl.PreferenceKeys.FIRST_LAUNCH

class PreferenceImpl(private val pref: SharedPreferences) : IPreference {

    private val util: TypePreferenceUtil by lazy { TypePreferenceUtil(pref) }

    override fun isFirst() = util.loadBoolean(FIRST_LAUNCH, true)

    override fun setIsFirst() = util.saveBoolean(FIRST_LAUNCH, false)

    override fun clear() = util.saveBoolean(FIRST_LAUNCH, true)

    object PreferenceKeys {
        const val FIRST_LAUNCH = "first_launch"
    }

    private class TypePreferenceUtil(private val sharedPreferences: SharedPreferences) {
        fun loadBoolean(key: String, defaultValue: Boolean = false): Boolean = sharedPreferences.getBoolean(key, defaultValue)

        fun saveBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()
    }
}
