package site.duqian.test.utils

import android.os.Environment
import java.io.DataOutputStream
import java.io.OutputStream

/**
 * description:执行adb命令，不能加adb shell ，否则总报错：device not found
 * @author 杜乾 Created on 2018/7/12 - 14:15.
 * E-mail:duqian2010@gmail.com
 */
object AdbUtil {

    private val TAG = AdbUtil::class.java.simpleName

    fun swipeScreen(left: Int, top: Int, right: Int, bottom: Int): Boolean {
        val command = "input swipe $left $top $right $bottom"
        val commandResult = ShellUtil.execCommand(command, true)
        val result = commandResult.result
        if (0 != result) {
            execShell(command)
        }
        //LogUtils.d(TAG, "swipeScreen = $command,result =$commandResult")
        return result == 0
    }

    fun screenshot(): Boolean {
        val command = "screencap -p " + Environment.getExternalStorageDirectory() + "screenshot.png"
        val commandResult = ShellUtil.execCommand(command, true)
        val result = commandResult.result
        if (0 != result) {
            execShell(command)
        }
        LogUtils.d(TAG, "screenshot = $command,result =$commandResult")
        return result == 0
    }

    fun excuteCommand(cmd: String) {
        var process: Process? = null
        var out: OutputStream? = null
        try {
            process = Runtime.getRuntime().exec("su")
            out = process!!.outputStream
            out.write(cmd.toByteArray())
            out.flush()
        } catch (e: Exception) {
            LogUtils.d(TAG, "excuteCommand=" + e.toString())
        } finally {
            try {
                out?.close()
                process?.destroy()
            } catch (e: Exception) {
            }
        }
    }

    private fun execShell(cmd: String) {
        try {
            val p = Runtime.getRuntime().exec("sh")  //su为root用户,sh普通用户
            val outputStream = p.outputStream
            val dataOutputStream = DataOutputStream(outputStream)
            dataOutputStream.writeBytes(cmd)
            dataOutputStream.flush()
            dataOutputStream.close()
            outputStream.close()
        } catch (t: Exception) {
            t.printStackTrace()
        }
    }
}
