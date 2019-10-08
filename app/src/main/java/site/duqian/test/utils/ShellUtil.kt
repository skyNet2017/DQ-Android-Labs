package site.duqian.test.utils

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.OutputStream

/**
 * description:shell命令
 * @author 杜乾 Created on 2018/7/12 - 11:42.
 * E-mail:duqian2010@gmail.com
 */
class ShellUtil private constructor() {

    private var os: OutputStream? = null

    init {
        throw AssertionError()
    }

    companion object {
        val COMMAND_SU = "su"
        val COMMAND_SH = "sh"
        val COMMAND_EXIT = "exit\n"
        val COMMAND_LINE_END = "\n"

        fun checkRootPermission(): Boolean {
            return execCommand("echo root", true, false).result == 0
        }

        fun execCommand(command: String, isRoot: Boolean): CommandResult {
            return executeCommand(arrayOf(command), isRoot, true)
        }


        fun execCommand(command: String, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
            return executeCommand(arrayOf(command), isRoot, isNeedResultMsg)
        }

        fun executeCommand(commands: Array<Any>?, isRoot: Boolean, isNeedResultMsg: Boolean): CommandResult {
            var result = -1
            if (commands == null || commands.size == 0) {
                return CommandResult(result)
            }
            var process: Process? = null
            var successResult: BufferedReader? = null
            var errorResult: BufferedReader? = null
            var successMsg: StringBuilder? = null
            var errorMsg: StringBuilder? = null
            var os: DataOutputStream? = null
            try {
                process = Runtime.getRuntime().exec(if (isRoot) COMMAND_SU else COMMAND_SH)
                os = DataOutputStream(process!!.outputStream)
                for (command in commands) {
                    os.write(command.toString().toByteArray())
                    os.writeBytes(COMMAND_LINE_END)
                    os.flush()
                }
                os.writeBytes(COMMAND_EXIT)
                os.flush()

                result = process.waitFor()

                if (isNeedResultMsg) {
                    successMsg = StringBuilder()
                    errorMsg = StringBuilder()

                    val allSuccessText = process.inputStream.bufferedReader().use(BufferedReader::readText)
                    successMsg.append(allSuccessText)

                    val allErrorText = process.errorStream.bufferedReader().use(BufferedReader::readText)
                    errorMsg.append(allErrorText)
                }
            } catch (e: Exception) {
                //e.printStackTrace()
            } finally {
                try {
                    os?.close()
                    successResult?.close()
                    errorResult?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                process?.destroy()
            }
            return CommandResult(result, successMsg?.toString() + "", errorMsg?.toString() + "")
        }
    }

}

