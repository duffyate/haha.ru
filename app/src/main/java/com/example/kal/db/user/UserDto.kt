package com.example.kal.db.user

import com.example.kal.utils.MyDateSerializer.serializeDateToString
import com.example.kal.utils.TimeManager.convertToLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDot (
    @SerialName("id") val id: Int? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("password") val password: String? = null,
    @SerialName("address") val address: String? = null,
    @SerialName("created_at") val date: String? = null
)

fun UserDot.toUser(): User {
    return User(
        id = id?: -1,
        firstName = firstName?: "",
        lastName = lastName?: "",
        email = email?: "",
        phone = phone?: "",
        password = password?: "",
        address = address?: "",
        date = serializeDateToString(convertToLocalDateTime(date!!))
    )
}