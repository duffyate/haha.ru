package com.example.kal.db.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") var id: Int = -1,
    @SerialName("first_name") var firstName: String = "",
    @SerialName("last_name") var lastName: String = "",
    @SerialName("email") var email: String = "",
    @SerialName("phone") var phone: String = "",
    @SerialName("password") var password: String = "",
    @SerialName("address") var address: String = "",
    @SerialName("created_at") var date: String = ""
)

fun User.toUserPost(): UserPost {
    return UserPost(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        password = password,
        address = address
    )
}