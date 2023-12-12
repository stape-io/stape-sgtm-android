package io.stape.sgtm.models

import com.google.gson.annotations.SerializedName

/**
 * The base structure with pre-defined properties for event payload.
 * Feel free to extend it for your needs.
 */
open class EventData(
    val clientId: String? = null,
    val currency: String? = null,
    val ipOverride: String? = null,
    val language: String? = null,
    val pageEncoding: String? = null,
    val pageHostname: String? = null,
    val pageLocation: String? = null,
    val pagePath: String? = null,
    val pageReferrer: String? = null,
    val pageTitle: String? = null,
    val screenResolution: String? = null,
    val userAgent: String? = null,
    var userData: UserData? = null,
    val userId: String? = null,
    val value: Int? = null,
    val viewportSize: String? = null,
) {
    @SerializedName("event_name")
    internal var eventName: String? = null

    @SerializedName("v")
    internal var protocolVersion: Int? = null
}
