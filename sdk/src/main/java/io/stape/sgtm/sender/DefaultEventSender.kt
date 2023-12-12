package io.stape.sgtm.sender

import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

internal const val clientIdHeaderName = "stape-client-id"
internal const val clientIdHeaderValue = "android-sdk"
internal const val eventParameterName = "event_name"

internal const val jsonMediaType = "application/json; charset=utf-8"

class DefaultEventSender(
    private val collectDataUrl: HttpUrl,
    private val client: OkHttpClient,
) : EventSender {

    override fun sendEvent(name: String, payload: ByteArray): Result<ByteArray> = runCatching {
        val response = client.newCall(
            Request.Builder()
                .addHeader(clientIdHeaderName, clientIdHeaderValue)
                .url(
                    collectDataUrl
                        .newBuilder()
                        .addQueryParameter(eventParameterName, name)
                        .build()
                )
                .post(payload.toRequestBody(jsonMediaType.toMediaType()))
                .build()
        ).execute()
        return@runCatching response.use {
            if (response.isSuccessful) {
                response.body?.bytes() ?: throw IllegalStateException("Response body is null")
            } else {
                throw IllegalStateException("Response code: ${response.code}")
            }
        }
    }
}
