package com.example.kal.db.vacancy

import kotlinx.serialization.SerialName

data class VacancyPost(
    @SerialName("job") val job: String? = null,
    @SerialName("money")val money: Int? = null,
    @SerialName("organization")val organization: String? = null,
    @SerialName("duties")val duties: String? = null,
    @SerialName("requirements")val requirements: String? = null,
    @SerialName("conditions")val conditions: String? = null,
    @SerialName("address")val address: String? = null
)
