package site.duqian.test.utils

import android.content.Context


/**
 * description:UI工具类
 * @author 杜乾 Created on 2018/7/12 - 14:14.
 * E-mail:duqian2010@gmail.com
 */
object UIUtil {

    fun dip2px(context: Context, dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

}


