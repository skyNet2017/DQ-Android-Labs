package site.duqian.test.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Description:获取设备，系统相关方法
 * @author 杜乾,Created on 2018/7/19 - 14:23.
 * E-mail:duqian2010@gmail.com
 */
class DeviceUtil {
    private var context: Context? = null
    private var initialVal = ""
    private var tm: TelephonyManager? = null

    constructor(context: Context) {
        this.context = context
        tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    fun getBuildVersionSDK(): Int {
        var result = 0
        try {
            result = Build.VERSION.SDK_INT
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun getModel(): String {
        var result: String? = initialVal
        try {
            result = Build.MODEL
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return handleIllegalCharacterInResult(result!!)
    }

    fun getBuildBrand(): String {
        var result: String? = initialVal
        try {
            result = Build.BRAND
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return handleIllegalCharacterInResult(result!!)
    }

    fun getIMEI(): String {
        var result: String? = initialVal
        val hasReadPhoneStatePermission = context!!.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        try {
            if (hasReadPhoneStatePermission) result = tm!!.getDeviceId()
        } catch (e: Exception) {
            e.printStackTrace()
            result = initialVal
        }
        return result!!
    }


    fun getIMSI(): String {
        var result: String? = initialVal
        val hasReadPhoneStatePermission = context!!.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        try {
            if (hasReadPhoneStatePermission) result = tm!!.getSubscriberId()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result!!
    }

    fun getManufacturer(): String {
        var result: String? = initialVal
        try {
            result = Build.MANUFACTURER
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (result == null || result.length == 0) {
            result = initialVal
        }
        return handleIllegalCharacterInResult(result)
    }

    fun getResolution(): String {
        var result = initialVal
        try {
            val wm = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            result = metrics.widthPixels.toString() + "*" + metrics.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (result.length == 0) {
            result = initialVal
        }
        return result
    }

    fun getOSVersion(): String {
        var result: String? = initialVal
        try {
            result = Build.VERSION.RELEASE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (result == null || result.length == 0) {
            result = initialVal
        }
        return result
    }


    private fun handleIllegalCharacterInResult(result: String): String {
        var result = result
        if (result.indexOf(" ") > 0) {
            result = result.replace(" ".toRegex(), "_")
        }
        return result
    }


}