package io.stape.sgtm.sender

interface EventSender {
    fun sendEvent(name: String, payload: ByteArray): Result<ByteArray>
}
