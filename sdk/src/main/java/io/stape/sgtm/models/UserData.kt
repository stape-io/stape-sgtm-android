package io.stape.sgtm.models

/**
 * The recommended data structure for user data.
 * Feel free to extend it for your needs.
 */
open class UserData(
    val emailAddress: String? = null,
    val sha256EmailAddress: String? = null,
    val phoneNumber: String? = null,
    val sha256PhoneNumber: String? = null,
    val address: Address? = null,
)
