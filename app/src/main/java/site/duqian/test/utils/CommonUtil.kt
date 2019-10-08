package site.duqian.test.utils

import java.net.URLDecoder

/**
 * Description:
 * @author 杜乾,Created on 2018/7/24 - 09:27.
 * E-mail:duqian2010@gmail.com
 */
object CommonUtil {

    inline fun <reified T> toArray(list: List<*>): Array<T> {
        return (list as List<T>).toTypedArray()
    }

    fun decodeToUtf8(str: String): String {
        val newStr = URLDecoder.decode(str, "UTF-8")
        return newStr
    }

    /**
     * unicode解码（unicode编码转中文）
     */
    fun unicodeDecode(theString: String): String {
        var aChar: Char
        val len = theString.length
        val outBuffer = StringBuffer(len)
        var x = 0
        while (x < len) {
            aChar = theString[x++]
            if (aChar == '\\') {
                aChar = theString[x++]
                if (aChar == 'u') {
                    // Read the xxxx
                    var value = 0
                    for (i in 0..3) {
                        aChar = theString[x++]
                        when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> value = (value shl 4) + aChar.toInt() - '0'.toInt()
                            'a', 'b', 'c', 'd', 'e', 'f' -> value = (value shl 4) + 10 + aChar.toInt() - 'a'.toInt()
                            'A', 'B', 'C', 'D', 'E', 'F' -> value = (value shl 4) + 10 + aChar.toInt() - 'A'.toInt()
                            else -> throw IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.")
                        }
                    }
                    outBuffer.append(value.toChar())
                } else {
                    if (aChar == 't')
                        aChar = '\t'
                    else if (aChar == 'r')
                        aChar = '\r'
                    else if (aChar == 'n')
                        aChar = '\n'
                    /*else if (aChar == 'f')
                        aChar = '\f'*/
                    outBuffer.append(aChar)
                }
            } else
                outBuffer.append(aChar)
        }
        return outBuffer.toString()
    }

}