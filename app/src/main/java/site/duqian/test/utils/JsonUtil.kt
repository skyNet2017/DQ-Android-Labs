package site.duqian.test.utils

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * description: json解析
 * @author 杜乾 Created on 2018/7/12 - 10:44.
 * E-mail:duqian2010@gmail.com
 */
object JsonUtil {

    fun <T> json2Bean(json: String, clazz: Class<T>): T {
        val gson = Gson()
        return gson.fromJson(json, clazz)
    }

    fun <T> parseEntityFromJson(json: String, clazz: Class<T>): T? {
        var result: T? = null
        try {
            result = Gson().fromJson(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * object : TypeToken<List<BaseEntity>>() {}.type
     */
    fun <T> parseListFromJson(json: String, type: Type): List<T>? {
        var result: List<T>? = null
        try {
            result = Gson().fromJson<List<T>>(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }
}
