package io.stape.sgtm

/**
 * Options for the SDK.
 *
 * @param sgtmServerHost the SGTM server host. Example: "sgtm.stape.io".
 * @param requestPath the request path.
 * @param protocolVersion the protocol version.
 * @param richsstsse the richsstsse flag.
 */
data class Options(
    val sgtmServerHost: String,
    val requestPath: String = "data",
    val protocolVersion: Int = 2,
    val richsstsse: Boolean = false,
)
