package io.stape.sgtm.utils

import android.net.TrafficStats
import io.stape.sgtm.utils.NamedThreadFactory.Companion.executorsDefault
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

/**
 * A [ThreadFactory] that sets the thread tag to the current thread
 * count. It uses decorator pattern.
 *
 * The purpose of this factory to build thread which will access network. It's required
 * by OS for network traffic monitoring.
 *
 * @see executorsDefault
 * @param threadFactory The decorated thread factory.
 */
internal class NamedThreadFactory(
    private val threadFactory: ThreadFactory
) : ThreadFactory {

    private var tag = "sgtm".hashCode()
    override fun newThread(r: Runnable?): Thread {
        return threadFactory.newThread {
            TrafficStats.setThreadStatsTag(tag++)
            r?.run()
        }
    }

    companion object {

        /**
         * Creates a [NamedThreadFactory] with the default thread factory.
         * @see Executors.defaultThreadFactory
         *
         * @return A [NamedThreadFactory] with the default thread factory.
         */
        fun executorsDefault() = NamedThreadFactory(Executors.defaultThreadFactory())
    }
}
