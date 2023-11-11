package io.stape.sgtm

import android.app.Application
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import io.stape.sgtm.firebase.FirebaseAnalyticsAdapter
import io.stape.sgtm.utils.InternalAppEventLogger
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY

class App : Application() {

    val eventStorage = EventStorage()

    val stape: Stape by lazy {
        Stape.withOption(
            options = Options(BuildConfig.STAPE_SERVER_HOST)
        ) {
            addInterceptor(
                HttpLoggingInterceptor(InternalAppEventLogger(eventStorage)).setLevel(BODY)
            )
        }
    }

    private val firebaseAnalytics by lazy { Firebase.analytics }

    /**
     * The decorated Firebase Analytics instance.
     * It will send all events to Firebase and SGTM server.
     */
    val analytics: FirebaseAnalyticsAdapter by lazy {
        FirebaseAnalyticsAdapter(stape, firebaseAnalytics).apply {
            setUserId("userId")
            setUserProperty("Name", "user 001")
            setUserProperty("address", "address 001")
            setConsent(emptyMap())
            setAnalyticsCollectionEnabled(true)
            setDefaultEventParameters(Bundle.EMPTY)
            setSessionTimeoutDuration(60 * 1000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder().detectAll().penaltyLog().build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder().detectAll().penaltyLog().build()
        )
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
