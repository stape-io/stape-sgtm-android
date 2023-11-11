package io.stape.sgtm.serialization

/**
 * A serializer that can serialize and deserialize data.
 */
interface Serializer {

    /**
     * Serialize data to byte array.
     * @param data the data to serialize.
     * @return the serialized data.
     */
    fun serialize(data: Any): ByteArray

    /**
     * Deserialize data from byte array.
     * @param data the data to deserialize.
     * @return the deserialized data.
     */
    fun deserialize(data: ByteArray): Map<String, Any>
}

