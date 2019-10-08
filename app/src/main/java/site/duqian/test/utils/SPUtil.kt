package site.duqian.test.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build

/**
 * description:保存自身app配置信息
 *
 * @author 杜乾 Created on 2018/7/12 - 10:46.
 * E-mail:duqian2010@gmail.com
 */
object SPUtil {
    private var sp: SharedPreferences? = null
    val SP_NAME = "config"
    private val MODE = if (Build.VERSION.SDK_INT >= 24) Context.MODE_PRIVATE else Context.MODE_WORLD_READABLE

    @SuppressLint("ApplySharedPref")
    fun putSP(context: Context?, key: String, value: Any) {
        if (context == null) return
        val type = value.javaClass.simpleName
        val sharedPreferences = context.getSharedPreferences(SP_NAME, MODE)
        val editor = sharedPreferences!!.edit()
        when (type) {
            "Integer" -> editor.putInt(key, value as Int)
            "Boolean" -> editor.putBoolean(key, value as Boolean)
            "String" -> editor.putString(key, value as String)
            "ADloat" -> editor.putFloat(key, value as Float)
            "Long" -> editor.putLong(key, value as Long)
        }
        editor.apply()
    }

    fun getSP(context: Context?, key: String, defValue: Any): Any? {
        if (context == null) return null
        val type = defValue.javaClass.simpleName
        val sharedPreferences = context.getSharedPreferences(SP_NAME, MODE)
        return when (type) {
            "Integer" -> sharedPreferences.getInt(key, defValue as Int)
            "Boolean" -> sharedPreferences.getBoolean(key, defValue as Boolean)
            "String" -> sharedPreferences.getString(key, defValue as String)
            "Float" -> sharedPreferences.getFloat(key, defValue as Float)
            "Long" -> sharedPreferences.getLong(key, defValue as Long)
            else -> null
        }
    }

    /*private fun getSP(context: Context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, MODE)
        }
    }

    fun putBoolean(context: Context, key: String, value: Boolean?) {
        getSP(context)
        sp!!.edit().putBoolean(key, value!!).apply()
    }

    fun getBoolean(context: Context, key: String, defValue: Boolean?): Boolean {
        getSP(context)
        return sp!!.getBoolean(key, defValue!!)
    }*/

}
