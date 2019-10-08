package site.duqian.test.utils

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.text.TextUtils
import java.io.*
import java.nio.channels.FileChannel
import java.util.*

/**
 * Description:
 * @author 杜乾,Created on 2018/7/15 - 10:05.
 * E-mail:duqian2010@gmail.com
 */

object FileUtil {

    fun isSdcardExist(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

    fun copyAssetsDirectory(context: Context, fromAssetPath: String, toPath: String): Boolean {
        try {
            val assetManager = context.assets
            val files = context.assets.list(fromAssetPath)
            if (isFileExist(toPath)) {
                deleteFile(toPath)
            } else {
                File(toPath).mkdirs()
            }
            var res = true
            for (file in files)
                if (file.contains(".")) {
                    res = res and copyAssetFile(assetManager, "$fromAssetPath/$file", "$toPath/$file")
                } else {
                    res = res and copyAssetsDirectory(context, "$fromAssetPath/$file", "$toPath/$file")
                }
            return res
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun copyAssetFile(assetManager: AssetManager?, fromAssetPath: String, toPath: String): Boolean {
        if (assetManager == null || TextUtils.isEmpty(fromAssetPath) || TextUtils.isEmpty(toPath)) {
            return false
        }
        try {
            deleteFile(toPath)
            File(toPath).parentFile.mkdirs()
            val inputStream = assetManager.open(fromAssetPath)
            val bis = BufferedInputStream(inputStream)
            val fos = FileOutputStream(toPath)
            val buf = ByteArray(1024)
            var read: Int = bis.read(buf)
            while (read != -1) {
                fos.write(buf, 0, read)
                read = bis.read(buf)
            }
            fos.flush()
            IOUtils.closeQuietly(fos)
            IOUtils.closeQuietly(bis)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }


    fun deleteFile(filePath: String) {
        if (TextUtils.isEmpty(filePath)) {
            return
        }
        try {
            val f = File(filePath)
            if (f.exists() && f.isDirectory) {
                val delFiles = f.listFiles()
                if (delFiles != null && delFiles.isNotEmpty()) {
                    for (i in delFiles.indices) {
                        deleteFile(delFiles[i].absolutePath)
                    }
                }
            }
            f.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isFileExist(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        var exist: Boolean
        try {
            val file = File(path)
            exist = file.exists() && file.length() > 0
        } catch (e: Exception) {
            exist = false
        }
        return exist
    }

    fun rename(path: String, newName: String): Boolean {
        var result = false
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
            return result
        }
        try {
            val file = File(path)
            if (file.exists()) {
                result = file.renameTo(File(newName))
            }
        } catch (e: Exception) {
        }

        return result
    }

    fun listFiles(filePath: String): Array<File>? {
        val file = File(filePath)
        return if (file.exists() && file.isDirectory) {
            file.listFiles()
        } else null
    }

    fun makeDirs(filePath: String?) {
        val file = File(filePath)
        if (file.exists()) {
            return
        }
        if (file.isFile) {
            val parentFile = file.parentFile
            if (parentFile.exists()) {
                return
            }
            parentFile.mkdirs()
        } else {
            file.mkdirs()
        }
    }

    fun readData(filePath: String): ByteArray {
        return readData(File(filePath))
    }

    fun readData(file: File): ByteArray {
        var content: ByteArray? = null
        if (file.exists()) {
            var `in`: FileInputStream? = null
            try {
                content = ByteArray(file.length().toInt())
                `in` = FileInputStream(file)
                val len = `in`.read(content)
                if (len == 0) {
                    content = null
                }
            } catch (e: Exception) {
                content = null
            } finally {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (e: Exception) {
                    }

                }
            }
        }
        if (content == null) {
            content = ByteArray(0)
        }
        return content
    }

    fun getString(filePath: String): String {
        return String(readData(filePath))
    }

    fun getString(file: File): String {
        return String(readData(file))
    }

    fun getStringFromAssets(context: Context, file: String): String {
        try {
            val inputStream = context.assets.open(file)
            val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
            val bufferedReader = BufferedReader(inputStreamReader)
            val lines = LinkedList<String>()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                lines.add(line)
                line = bufferedReader.readLine()
            }
            val separator = System.getProperty("line.separator")
            return TextUtils.join(separator, lines)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun writeStringToFile(filePath: String, append: Boolean, text: String): Boolean {
        return if (TextUtils.isEmpty(text)) {
            false
        } else {
            try {
                val f = File(filePath)
                if (!f.parentFile.exists() && !f.parentFile.mkdirs()) {
                    false
                } else {
                    val out = BufferedWriter(FileWriter(filePath, append))
                    try {
                        out.write(text)
                    } finally {
                        out.flush()
                        out.close()
                    }

                    true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    /**
     * 获取视频时长，耗时几十毫秒
     *
     * @param videoPath
     * @return
     */
    fun getVideoDuration(videoPath: String): Long {
        if (TextUtils.isEmpty(videoPath)) {
            return 0
        }
        var durationInt: Long = 0
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoPath)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            durationInt = Integer.parseInt(duration).toLong()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return durationInt
    }

    fun copyFileByFileChannels(fromPath: String?, destPath: String?): Boolean {
        val source = File(fromPath)
        if (!source.exists()) {
            return false
        }
        val dest = File(destPath)
        if (!dest.exists()) {
            dest.delete()
        }
        var ret = false
        var inputChannel: FileChannel? = null
        var outputChannel: FileChannel? = null
        try {
            val parentFile = dest.parentFile
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs()
            }
            inputChannel = FileInputStream(source).channel
            outputChannel = FileOutputStream(dest).channel
            outputChannel!!.transferFrom(inputChannel, 0L, inputChannel!!.size())
            ret = true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            IOUtils.closeQuietly(inputChannel)
            IOUtils.closeQuietly(outputChannel)
        }
        return ret
    }

    /**
     * 生成缩略图
     *
     * @param videoPath
     * @param targetThumb
     * @return
     */
    fun createVideoThumbnail(videoPath: String, targetThumb: String): Boolean {
        deleteFile(targetThumb)
        val bitmap = getVideoThumbnailBitmap(videoPath, 5)
        val bitmapToFile = bitmapToFile(bitmap, targetThumb)
        if (!bitmapToFile) {
            deleteFile(targetThumb)
        }
        return bitmapToFile
    }

    private fun bitmapToFile(bitmap: Bitmap?, imagePath: String): Boolean {
        deleteFile(imagePath)
        if (bitmap == null) {
            return false
        }
        var out: FileOutputStream? = null
        try {
            val file = File(imagePath)
            val fileFolder = file.parentFile
            if (fileFolder != null && !fileFolder.exists()) {
                fileFolder.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            out = FileOutputStream(file)
            val compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            return compress
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            IOUtils.closeQuietly(out)
        }
        return false
    }

    fun getVideoThumbnailBitmap(filePath: String, timeUs: Long): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(filePath)
            bitmap = retriever.getFrameAtTime(timeUs)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

}