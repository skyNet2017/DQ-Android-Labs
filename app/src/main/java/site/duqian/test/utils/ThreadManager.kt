package site.duqian.test.utils

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy
import java.util.concurrent.TimeUnit

/**
 * description:线程池
 * @author 杜乾 Created on 2018/7/12 - 10:41.
 * E-mail:duqian2010@gmail.com
 */
object ThreadManager {

    private var mBackgroundUploadPool: ThreadPoolProxy? = null
    private val mBackgroundUploadLock = Any()

    val backgroundPool: ThreadPoolProxy
        get() = synchronized(mBackgroundUploadLock) {
            if (mBackgroundUploadPool == null) {
                mBackgroundUploadPool = ThreadPoolProxy(5, 5, 5L)
            }
            return mBackgroundUploadPool as ThreadPoolProxy
        }

    class ThreadPoolProxy(private val mCorePoolSize: Int, private val mMaximumPoolSize: Int, private val mKeepAliveTime: Long) {
        private var mPool: ThreadPoolExecutor? = null

        @Synchronized
        fun execute(run: Runnable?) {
            if (run == null) {
                return
            }
            if (mPool == null || mPool!!.isShutdown) {
                mPool = ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, mKeepAliveTime, TimeUnit.MILLISECONDS, LinkedBlockingQueue(), Executors.defaultThreadFactory(), AbortPolicy())
            }
            mPool!!.execute(run)
        }

        @Synchronized
        fun cancel(run: Runnable) {
            if (mPool != null && (!mPool!!.isShutdown || mPool!!.isTerminating)) {
                mPool!!.queue.remove(run)
            }
        }

        @Synchronized
        operator fun contains(run: Runnable): Boolean {
            return if (mPool != null && (!mPool!!.isShutdown || mPool!!.isTerminating)) {
                mPool!!.queue.contains(run)
            } else {
                false
            }
        }

        /** 立刻关闭线程池，并且正在执行的任务也将会被中断  */
        @Synchronized
        fun stop() {
            if (mPool != null && (!mPool!!.isShutdown || mPool!!.isTerminating)) {
                mPool!!.shutdown()
            }
        }

        /** 平缓关闭单任务线程池，但是会确保所有已经加入的任务都将会被执行完毕才关闭  */
        @Synchronized
        fun shutdown() {
            if (mPool != null && (!mPool!!.isShutdown || mPool!!.isTerminating)) {
                mPool!!.shutdownNow()
            }
        }
    }
}
