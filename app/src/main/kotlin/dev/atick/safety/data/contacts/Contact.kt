package dev.atick.safety.data.contacts

data class Contact(
    val name: String,
    val number: String,
    val email: String? = null,
    val highRisk: Boolean = false,
    val selected: Boolean = false
)
