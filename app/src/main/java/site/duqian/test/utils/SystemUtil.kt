package site.duqian.test.utils

import android.app.ActivityManager
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri 
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * description:系统工具类
 *
 * @author 杜乾 Created on 2018/7/12 - 10:51.
 * E-mail:duqian2010@gmail.com
 */
object SystemUtil {

    val time: String
        get() {
            val dateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
            dateFormat.applyPattern("HH:mm:ss:SS")
            return dateFormat.format(Date(System.currentTimeMillis()))
        }

    /**
     * 检测手机是否装有 xposed
     */
    fun isXposedInstalled(context: Context): Boolean {
        val packageManager = context.packageManager ?: return false
        val packages = packageManager.getInstalledPackages(0)
        if (packages != null && packages.size > 0) {
            for (i in packages.indices) {
                val packageInfo = packages[i]
                val packageName = packageInfo.packageName
                if (packageName.contains("de.robv.android.xposed.installer")) {
                    return true
                }
            }
        }
        return false
    }

    fun isProcessRunning(context: Context, processName: String): Boolean {
        val mActivityManager = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in mActivityManager.runningAppProcesses) {
            val processName1 = appProcess.processName
            if (processName1 == processName) {
                return true
            }
        }
        return false
    }


    /**
     * 判断服务是否正在运行
     */
    fun isServiceRunning(context: Context, clazz: Class<*>): Boolean {
        return isServiceRunning(context, clazz.name)
    }

    private fun isServiceRunning(context: Context, className: String): Boolean {
        var isRunning = false
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val serviceList = activityManager.getRunningServices(Integer.MAX_VALUE)
        if (serviceList == null || serviceList.size == 0) {
            return false
        }
        for (i in serviceList.indices) {
            if (serviceList[i].service.className == className) {
                isRunning = true
                break
            }
        }
        return isRunning
    }

    fun copyText(context: Context?, content: String) {
        if (TextUtils.isEmpty(content) || context == null) {
            return
        }
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (!TextUtils.isEmpty(content)) {
            cmb.text = content //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
        }
    }

    fun getCopiedText(context: Context?): String {
        if (context == null) {
            return ""
        }
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return cmb.text.toString()
    }

    /**
     * 播放声音
     */
    fun sound(context: Context) {
        try {
            val player = MediaPlayer.create(context,
                    Uri.parse("file:///system/media/audio/ui/camera_click.ogg"))
            player.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getVersionCode(context: Context?): Int {
        val packageInfo = getPackageInfo(context, context!!.packageName)
        return if (packageInfo != null) packageInfo.versionCode else 0
    }

    fun getVersionName(context: Context?): String {
        val packageInfo = getPackageInfo(context, context!!.packageName)
        return if (packageInfo != null) packageInfo.versionName else ""
    }

    fun getPackageInfo(context: Context?, packageName: String): PackageInfo? {
        if (context == null || packageName.isEmpty()) return null
        var pi: PackageInfo? = null
        synchronized(SystemUtil::class.java) {
            try {
                val pm = context.packageManager
                pi = pm.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return pi
    }

}
