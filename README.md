# Android Stape SDK

Stape SDK is an event based client for the Stape API. It allows to track and send events from your
mobile app to the Stape backend.

## Overview

SDK implements two main functional parts.
First is a simple event send which allows to compose an event and send it to the backend.
SDK API provides a response from the backend mapped to a simple model that contains serialized
response fields.
Second is a mechanism that tracks events dispatched by the Firebase event SDK.
It hooks Firebase event dispatch and maps Firebase event to Stape event.
After that it sends it as an ordinary event.

## Usage

### Initialization

To use the Stape SDK you need to create an instance of the `Stape` class.
The instance can be created using the `Stape.withOption` method.
The method takes an `Options` object as a parameter.

```kotlin
import io.stape.sgtm.Options
import io.stape.sgtm.Stape

val stape = Stape.withOption(Options("yourdomain.com"))
```

The `Options` object contains data for correct SDK initialization and requires only one
parameter - `domain`.
The `domain` parameter is a domain name of your Stape instance. Do not include `https://`
or `http://` schemas.
Please, do not override the default `Options` values if you are not sure what you are doing.

### Sending events

After the Stape instance is created you can send events to the server.
Stape allows to send data in a couple of ways:

- Using a Map<String, Any> collection;
- Using a EventData class.

These two ways are equivalent and you can use any of them:

```kotlin
stape.sendEvent("event1", mapOf("userId" to "cdd1eac3e254", "language" to "en"))
stape.sendEvent("event2", EventData(userId = "cdd1eac3e254", language = "en"))
```

Please, extend the `EventData` class if you need to send any other data.

### Tracking Firebase events

Stape SDK allows to decorate Firebase Analytics instance and deliver all sent events to the Stape
backend as well.
For this purpose you need to use `FirebaseAnalyticsAdapter` class.

```kotlin
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import io.stape.sgtm.firebase.FirebaseAnalyticsAdapter
import io.stape.sgtm.Options
import io.stape.sgtm.Stape

private val stape = Stape.withOption(Options("yourdomain.com"))
private val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
val analytics = FirebaseAnalyticsAdapter(stape, firebaseAnalytics)

// Using Bundle as a parameter
analytics.logEvent("event3", Bundle().apply {
    putString("userId", "cdd1eac3e254")
    putString("language", "en")
})

// Using ParamBuilder as a parameter
analytics.logEvent("event4") {
    param("userId", "cdd1eac3e254")
    param("language", "en")
}
```

The `FirebaseAnalyticsAdapter` provides absolutely the same API as the Firebase Analytics instance.
It means that you can simple replace the provided types without a lot of changes.

## Trying sample app

If you desire to try the sample app, please, follow the next steps:

1. Clone the repository;
2. Open the project in Android Studio;
3. Create a Firebase project with enabled Analytics;
4. Generate your own `google-services.json` file and put it into the `app` folder;
5. Create `stape.properties` file in the `app` folder and put your Stape domain name into it as a
   value for `stape.url` property. Example: ```server.host=yourdomain.com```;
6. Rebuild the app.

## License

See [LICENSE](LICENSE) for more details.

