package io.stape.sgtm.screens.main

import io.stape.sgtm.models.EventData

class ExtendedEventData(
    val deviceType: String,
    val deviceManufacturer: String,
    clientId: String,
    screenName: String
) : EventData(clientId, screenName)
