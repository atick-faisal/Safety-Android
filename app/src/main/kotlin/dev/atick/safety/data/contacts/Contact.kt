package dev.atick.safety.data.contacts

typealias RoomContact = dev.atick.storage.room.data.models.Contact

data class Contact(
    val id: Long = 0L,
    val name: String,
    val phone: String,
    val email: String? = null,
    val highRisk: Boolean = false,
    val selected: Boolean = false
) {
    fun asRoomContact(): RoomContact {
        return RoomContact(
            id = id,
            name = name,
            phone = phone,
            email = email,
            highRisk = highRisk,
            selected = selected
        )
    }
}

fun RoomContact.asContact(): Contact {
    return Contact(
        id = id,
        name = name,
        phone = phone,
        email = email,
        highRisk = highRisk,
        selected = selected
    )
}