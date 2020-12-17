package com.yomplex.tests.utils

import android.content.Context
import android.content.SharedPreferences
import com.yomplex.tests.utils.ConstantPath.PREFS


class SharedPrefs {

    companion object {
        fun getSharedPref(mContext: Context): SharedPreferences {

            return mContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        }
    }

    fun setPrefVal(mContext: Context, key: String?, value: String) {
        if (key != null) {
            val edit = getSharedPref(mContext).edit()
            edit.putString(key, value)
            edit.apply()
        }
    }

    fun setIntPrefVal(mContext: Context, key: String?, value: Int) {
        if (key != null) {
            val edit = getSharedPref(mContext).edit()
            edit.putInt(key, value)
            edit.apply()
        }
    }

    fun setLongPrefVal(mContext: Context, key: String?, value: Long?) {
        if (key != null) {
            val edit = getSharedPref(mContext).edit()
            edit.putLong(key, value!!)
            edit.apply()
        }
    }

    fun setBooleanPrefVal(mContext: Context, key: String?, value: Boolean) {
        if (key != null) {
            val edit = getSharedPref(mContext).edit()
            edit.putBoolean(key, value)
            edit.apply()
        }
    }


    fun getPrefVal(mContext: Context, key: String): String? {
        val pref = getSharedPref(mContext)
        var value: String? = ""
        try {
            if (pref.contains(key))
                value = pref.getString(key, "")
            else
                value = ""
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return value
    }

    fun getIntPrefVal(mContext: Context, key: String): Int {
        val pref = getSharedPref(mContext)
        var value = 0
        try {
            if (pref.contains(key)) value = pref.getInt(key, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return value
    }

    fun getLongPrefVal(mContext: Context, key: String): Long? {
        val pref = getSharedPref(mContext)
        var value: Long? = null
        try {
            if (pref.contains(key)) value = pref.getLong(key, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return value
    }

    fun getBooleanPrefVal(mContext: Context, key: String): Boolean {
        val pref = getSharedPref(mContext)
        var value = false
        try {
            if (pref.contains(key)) value = pref.getBoolean(key, false)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return value
    }
}