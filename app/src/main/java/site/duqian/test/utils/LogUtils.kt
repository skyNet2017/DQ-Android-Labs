package site.duqian.test.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import de.robv.android.xposed.XC_MethodHook

/**
 * description:简单log封装
 * @author 杜乾 Created on 2018/7/12 - 10:34.
 * E-mail:duqian2010@gmail.com
 */
object LogUtils {
    //companion object {
    var TAG = "-xpdq-"
    private val debug = true;//BuildConfig.DEBUG

    fun d(Tag: String, value: String) {
        LogUtils.d("$Tag $value")
    }

    fun d(obj: Any, value: Any) {
        if (debug) {
            Log.d(obj.javaClass.simpleName + TAG, value.toString())
        }
    }

    fun d(msg: Any) {
        if (debug) {
            Log.d(TAG, msg.toString())
        }
    }

    fun v(msg: String) {
        if (debug) {
            Log.v(TAG, msg)
        }
    }

    fun d(msg: String) {
        if (debug) {
            Log.d(TAG, msg)
        }
    }

    fun d(clazz: Any, msg: String) {
        if (debug) {
            Log.d(Any::javaClass.name + TAG, msg)
        }
    }

    fun i(msg: String) {
        if (debug) {
            Log.i(TAG, msg)
        }
    }

    fun w(msg: String) {
        if (debug) {
            Log.w(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (debug) {
            Log.e("d-q", msg)
        }
    }

    fun logStackTraces(methodCount: Int = 15, methodOffset: Int = 3) {
        val trace = Thread.currentThread().stackTrace
        var level = ""
        LogUtils.d("---------logStackTraces start----------")
        for (i in methodCount downTo 1) {
            val stackIndex = i + methodOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].className)
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            level += "   "
            LogUtils.d(builder.toString())
        }
        LogUtils.d("---------logStackTraces end----------")
    }


    fun printMsgObj(msg: Any) {
        val fieldNames = msg::class.java.fields
        fieldNames.forEach {
            val field = it.get(msg)
            if (field is Array<*>) {
                val s = StringBuffer()
                field.forEach {
                    s.append(it.toString() + " , ")
                }
                LogUtils.d("$it = $s")
            } else {
                LogUtils.d("$it = $field")
            }
        }
    }


    fun printArgs(Tag: String, param: XC_MethodHook.MethodHookParam?) {
        try {
            val args = param?.args as Array
            if (args!!.isNotEmpty()) {
                for (index in args.indices) {
                    val any = args[index]
                    if (any is Array<*>) {
                        val sb = StringBuilder()
                        for (obj in any) {
                            sb.append("$Tag $index=$obj,")
                        }
                        LogUtils.d("$Tag $index=" + sb.toString())
                    } else {
                        LogUtils.d("$Tag $index=$any\n")
                    }
                }
            }
        } catch (e: Exception) {
            LogUtils.d("$Tag args error:$e\n")
        }
    }

    fun printIntent(tag: String, param: XC_MethodHook.MethodHookParam?) {
        try {
            val activity = param!!.thisObject as Activity
            val extras = activity.intent.extras
            if (extras != null) {
                val keySet = extras.keySet()
                for (key in keySet) {
                    LogUtils.d("$tag $key=${extras.get(key)}")
                }
            }
        } catch (e: Exception) {
            LogUtils.d("$tag  error $e")
        }
    }

    fun printIntentParams(tag: String, intent: Intent) {
        try {
            val extras = intent.extras
            if (extras != null) {
                val keySet = extras.keySet()
                for (key in keySet) {
                    LogUtils.d("$tag $key=${extras.get(key)}")
                }
            }
        } catch (e: Exception) {
            LogUtils.d("$tag  error $e")
        }
    }
    //}
}
