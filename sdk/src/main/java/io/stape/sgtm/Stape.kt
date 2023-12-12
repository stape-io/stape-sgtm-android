package io.stape.sgtm

import android.util.Log
import io.stape.sgtm.models.EventData
import io.stape.sgtm.models.UserData
import io.stape.sgtm.sender.DefaultEventSender
import io.stape.sgtm.sender.EventSender
import io.stape.sgtm.serialization.GsonSerializer
import io.stape.sgtm.serialization.Serializer
import io.stape.sgtm.utils.NamedThreadFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors.newSingleThreadExecutor

/**
 * The Stape SGTM integration.
 *
 * @param options the options which will be used for configuration.
 * @param serializer the serializer for event payload and SGTM responses.
 * @param executor the executor which will be used to send events.
 * @param eventSender the SGTM api client for sending events.
 */
class Stape internal constructor(
    private val options: Options,
    private val serializer: Serializer,
    private val executor: Executor,
    private val eventSender: EventSender,
) {

    /**
     * The default user data that will be attached to each event if no user data was provided inside.
     * Keep this field null if you're going to provide user data with each event.
     */
    var defaultUserData: UserData? = null

    /**
     * Send event to SGTM server.
     *
     * @param name the event name.
     * @param eventData the event data.
     * @return the completable future with the result received from SGTM.
     */
    fun sendEvent(name: String, eventData: Map<String, Any>) = sendSerializedEvent(name) {
        val payload = eventData.plus(
            listOf("v" to options.protocolVersion, "event_name" to name)
        )
        serializer.serialize(payload)
    }

    /**
     * Send event to SGTM server.
     *
     * @param name the event name.
     * @param eventData the event data.
     * @return the completable future with the result received from SGTM.
     */
    fun sendEvent(name: String, eventData: EventData) = sendSerializedEvent(name) {
        eventData.protocolVersion = options.protocolVersion
        eventData.eventName = name
        if (eventData.userData == null) {
            eventData.userData = defaultUserData
        }
        serializer.serialize(eventData)
    }

    /**
     * Internal method to send serialized event to SGTM server.
     *
     * @param name the event name.
     * @param payloadSupplier the payload supplier.
     * @return the completable future with the result received from SGTM.
     */
    private fun sendSerializedEvent(
        name: String,
        payloadSupplier: () -> ByteArray
    ): CompletableFuture<Result<Map<String, Any>>> {
        return CompletableFuture.supplyAsync({
            eventSender.sendEvent(name, payloadSupplier())
                .mapCatching { serializer.deserialize(it) }
                .onFailure { Log.d("StapeSGTM", "Failed to send event: $name", it) }
        }, executor)
    }

    companion object {

        /**
         * Create an instance of Stape SGTM integration.
         *
         * @param options the options which will be used for configuration.
         * @param clientConfigurationBlock the block for configuring the http client. Not required.
         * @return the Stape SGTM integration instance.
         */
        fun withOption(
            options: Options,
            clientConfigurationBlock: (OkHttpClient.Builder.() -> Unit)? = null
        ) = Stape(
            options = options,
            serializer = GsonSerializer(),
            executor = newSingleThreadExecutor(NamedThreadFactory.executorsDefault()),
            eventSender = DefaultEventSender(
                collectDataUrl = HttpUrl.Builder()
                    .scheme("https")
                    .host(options.sgtmServerHost)
                    .addPathSegment(options.requestPath)
                    .addQueryParameter("v", options.protocolVersion.toString())
                    .addQueryParameter("richsstsse", options.richsstsse.toString())
                    .build(),
                client = OkHttpClient.Builder().apply { clientConfigurationBlock?.invoke(this) }
                    .build()
            )
        )
    }
}
