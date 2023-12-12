package io.stape.sgtm.models

/**
 * The recommended data structure for user address.
 * Feel free to extend it for your needs.
 */
open class Address(
    val firstName: String? = null,
    val sha256FirstName: String? = null,
    val lastName: String? = null,
    val sha256LastName: String? = null,
    val street: String? = null,
    val city: String? = null,
    val region: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
)
