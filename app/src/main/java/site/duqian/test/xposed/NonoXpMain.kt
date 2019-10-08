package site.duqian.test.xposed

import android.content.Context
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import site.duqian.test.utils.LogUtils
import site.duqian.test.utils.ToastUtil

/**
 * Description:Main entry of hook modules
 * @author 杜乾,Created on 2018/7/11 - 11:34.
 * E-mail:duqian2010@gmail.com
 */
class NonoXpMain : IXposedHookLoadPackage {

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val processName = lpparam.processName
        val context = getContext()
        LogUtils.d("dq-hook handleLoadPackage processName $processName")
        ToastUtil.toast(context, processName + ",${lpparam.packageName}")
    }

    private fun getContext(): Context? {
        try {
            val aClass = XposedHelpers.findClass("android.app.ActivityThread", null)
            val `object` = arrayOfNulls<Any>(0)
            val currentActivityThread =
                XposedHelpers.callStaticMethod(aClass, "currentActivityThread", *`object`)
            LogUtils.d("dq-hook  AttachBaseContext!")
            return XposedHelpers.callMethod(
                currentActivityThread,
                "getSystemContext",
                *`object`
            ) as Context
        } catch (e: Exception) {
        }
        return null
    }

}