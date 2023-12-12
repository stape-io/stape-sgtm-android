package io.stape.sgtm.firebase

import android.os.BaseBundle
import android.os.Bundle
import androidx.annotation.Size
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import io.stape.sgtm.Stape
import io.stape.sgtm.utils.filterNullValues

/**
 * A wrapper class for Firebase Analytics.
 * It will send all events to Firebase and SGTM server.
 *
 * @param stape the Stape SGTM integration.
 * @param firebaseAnalytics the Firebase Analytics instance.
 */
class FirebaseAnalyticsAdapter(
    private val stape: Stape,
    private val firebaseAnalytics: FirebaseAnalytics
) {
    private var userId: String? = null
    private val userProperties: MutableMap<String, String> = mutableMapOf()
    private var defaultParameters: Bundle? = null

    fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
        this.userId = userId
    }

    fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
        userProperties[name] = value
    }

    fun setConsent(consent: Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus>) =
        firebaseAnalytics.setConsent(consent)

    fun setAnalyticsCollectionEnabled(enabled: Boolean) =
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)

    fun setDefaultEventParameters(parameters: Bundle?) {
        firebaseAnalytics.setDefaultEventParameters(parameters)
        defaultParameters = parameters
    }

    fun setSessionTimeoutDuration(milliseconds: Long) =
        firebaseAnalytics.setSessionTimeoutDuration(milliseconds)

    fun logEvent(@Size(min = 1L, max = 40L) name: String, params: Bundle?) {
        firebaseAnalytics.logEvent(name, params)

        val payloadBundle = Bundle()
        defaultParameters?.let(payloadBundle::putAll)
        params?.let(payloadBundle::putAll)
        userId?.let { payloadBundle.putString("userId", it) }

        var payloadMap = payloadBundle.toMap()
        payloadMap = payloadMap.plus("userData" to userProperties)
        stape.sendEvent(name, payloadMap)
    }

    fun logEvent(
        @Size(min = 1L, max = 40L) name: String,
        bundleBuilder: ParametersBuilder.() -> Unit
    ) = logEvent(name, ParametersBuilder().apply(bundleBuilder).bundle)

    /**
     * Converts a [Bundle] to a [Map].
     * @return the converted map.
     */
    @Suppress("DEPRECATION")
    private fun BaseBundle.toMap(): Map<String, Any> {
        return keySet().associateBy(
            keySelector = { it },
            // It's deprecated since API 33 (TIRAMISU). Replace it later.
            valueTransform = {
                convertBundleValue(get(it))
            }
        ).filterNullValues()
    }

    /**
     * Converts a [Bundle] value to a [Map] value.
     * It gets rid of nested [Bundle]s and [Array]s.
     *
     * @param value the value to convert.
     * @return the converted value.
     */
    private fun convertBundleValue(value: Any?): Any? = when (value) {
        is BaseBundle -> value.toMap()
        is Array<*> -> value.map(::convertBundleValue)
        else -> value
    }
}
