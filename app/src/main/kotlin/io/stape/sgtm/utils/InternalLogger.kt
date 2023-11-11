package io.stape.sgtm.utils

import io.stape.sgtm.EventStorage
import okhttp3.logging.HttpLoggingInterceptor

/**
 * The custom logger for OkHttp.
 * It collects client traffic logs to [EventStorage] and print it to logcat.
 *
 * @param eventStorage the event storage instance.
 */
class InternalAppEventLogger(private val eventStorage: EventStorage) : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        eventStorage.add(message)
        HttpLoggingInterceptor.Logger.DEFAULT.log(message)
    }
}
