package com.example.kal.db.vacancy

import com.example.kal.utils.MyDateSerializer.serializeDateToString
import com.example.kal.utils.TimeManager.convertToLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancyDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("job") val job: String? = null,
    @SerialName("money")val money: Int? = null,
    @SerialName("organization")val organization: String? = null,
    @SerialName("duties")val duties: String? = null,
    @SerialName("requirements")val requirements: String? = null,
    @SerialName("conditions")val conditions: String? = null,
    @SerialName("address")val address: String? = null,
    @SerialName("created_at") val date: String? = null
)


fun VacancyDto.toVacancy(): Vacancy {
    return Vacancy(
        id = id?: -1,
        job = job?: "",
        money = money?: 0,
        organization = organization?: "",
        duties = duties?: "",
        requirements = requirements?: "",
        conditions = conditions?: "",
        address = address?: "",
        date = serializeDateToString(convertToLocalDateTime(date!!))
    )
}
