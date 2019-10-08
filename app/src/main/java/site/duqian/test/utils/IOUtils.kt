package site.duqian.test.utils

import android.database.Cursor
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream

/**
 * description:
 * @author Dusan Created on 2018/8/13 - 10:39.
 * E-mail:duqian2010@gmail.com
 */
object IOUtils {

    private val DEFAULT_BUFFER_SIZE = 8 * 1024

    /**
     * 关闭流
     *
     * @param closeable 实现了[Closeable] 的类,像[InputStream],
     * [OutputStream]...
     */
    fun closeQuietly(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: Throwable) {
            }

        }
    }

    /**
     * 关闭数据库的游标
     *
     * @param cursor 数据库游标
     */
    fun closeQuietly(cursor: Cursor?) {
        if (cursor != null) {
            try {
                cursor.close()
            } catch (e: Throwable) {
            }

        }
    }

}
