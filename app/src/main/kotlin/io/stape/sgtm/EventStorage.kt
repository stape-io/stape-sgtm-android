package io.stape.sgtm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class EventStorage {
    private val eventsMutableFlow = MutableStateFlow<List<String>>(emptyList())

    val eventsFlow: Flow<List<String>> get() = eventsMutableFlow

    fun add(eventData: String) = eventsMutableFlow.update { it.plus(eventData) }

    fun clear() = eventsMutableFlow.update { emptyList() }
}
