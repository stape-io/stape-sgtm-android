package io.stape.sgtm.serialization

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.stape.sgtm.utils.filterNullValues

class GsonSerializer(private val gson: Gson = Gson()) : Serializer {
    override fun serialize(data: Any): ByteArray {
        return gson.toJson(data).toByteArray()
    }

    override fun deserialize(data: ByteArray): Map<String, Any> {
        val type = object : TypeToken<Map<String, Any?>>() {}

        return gson.fromJson(data.toString(Charsets.UTF_8), type).filterNullValues()
    }
}
