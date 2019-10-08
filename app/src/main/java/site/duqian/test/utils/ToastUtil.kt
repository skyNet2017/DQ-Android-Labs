package site.duqian.test.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * description:
 * @author 杜乾 Created on 2018/7/12
 * E-mail:duqian2010@gmail.com
 */
object ToastUtil {

    private var handler: Handler? = null
    private var weakHandler: Handler? = null
    private var isLong = false

    fun toast(context: Activity?, msg: String) {
        if ("main" == Thread.currentThread().name) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } else {
            context!!.runOnUiThread { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
        }
    }

    fun toastLong(context: Context?, vararg args: String) {
        isLong = true
        toast(context, *args)
    }

    fun toastShort(context: Context?, vararg args: String) {
        isLong = false
        toast(context, *args)
    }

    fun toast(context: Context?, vararg args: String) {
        //if (handler == null) {
        handler = Handler(Looper.getMainLooper())
        //}
        handler!!.post { makeText(context, *args) }
    }

    private fun makeText(context: Context?, vararg args: String) {
        if (context == null || args.isEmpty()) return
        val sb = StringBuilder()
        for (obj in args) {
            sb.append(obj).append(" ")
        }
        if (isLong) {
            Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun toastInWechat(context: Context?, vararg args: String) {
        if (weakHandler == null) {
            weakHandler = Handler(Looper.getMainLooper())
        }
        weakHandler!!.post { makeText(context, *args) }
    }
}
