package com.example.kal.db.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserVac(
    @SerialName("id") val id: Int? = null,
    @SerialName("user_id") val userId: Int? = null,
    @SerialName("vacanc_id") val VacId: Int? = null,
    @SerialName("created_at") val date: String? = null
)
