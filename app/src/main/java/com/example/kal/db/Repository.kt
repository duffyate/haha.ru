package com.example.kal.db

import com.example.kal.db.user.User
import com.example.kal.db.user.UserDot
import com.example.kal.db.user.toUser
import com.example.kal.db.user.toUserPost
import com.example.kal.db.vacancy.Vacancy
import com.example.kal.db.vacancy.VacancyDto
import com.example.kal.db.vacancy.toVacancy
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

suspend fun getAllUsers(): List<User> {
    return try {
        val response = client.get("$BASE_URL/users") {
            parameter("select", "*")
        }
        if (response.status.value in 200..299) {
            response.body<List<UserDot>>().map {
                it.toUser()
            }
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        println("Exception getAllUsers(): ${e.message}")
        emptyList()
    }
}

suspend fun getAllVacancies(): List<Vacancy> {
    return try {
        val response = client.get("$BASE_URL/vacancies") {
            parameter("select", "*")
        }
        if (response.status.value in 200..299) {
            response.body<List<VacancyDto>>().map {
                it.toVacancy()
            }
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        println("Exception getAllUsers(): ${e.message}")
        emptyList()
    }
}

suspend fun authUser(email: String, password: String): Boolean {
    return try {
        val response = client.get("$BASE_URL/users") {
            parameter("email", "eq.$email")
            parameter("password", "eq.$password")
        }
        if (response.status.value in 200..299) {
            response.status == HttpStatusCode.OK
        } else  {
            false
        }
    } catch (e: Exception) {
        println("Exception authUser(): ${e.message}")
        false
    }
}

suspend fun registerUser(user: User): Boolean {
    return try {
        val response = client.post("$BASE_URL/users") {
            setBody(user.toUserPost())
        }
        println("Body : ${response.bodyAsText()}")
        if (response.status.value in 200..299) {
            response.status == HttpStatusCode.Created
        } else  {
            false
        }
    } catch (e: Exception) {
        println("Exception registerUser(): ${e.message}")
        false
    }
}

suspend fun getUserIdByEmail(email: String): Int {
    return try {
        val response = client.get("$BASE_URL/users") {
            parameter("email", "eq.$email")
        }
        if (response.status.value in 200..299) {
            response.body<List<UserDot>>().first().id?: -1
        } else  {
            -1
        }
    } catch (e: Exception) {
        println("Exception getUserIdByEmail(): ${e.message}")
        -1
    }
}

suspend fun getUserByEmail(email: String): User {
    return try {
        val response = client.get("$BASE_URL/users") {
            parameter("email", "eq.$email")
        }
        if (response.status.value in 200..299) {
            response.body<List<UserDot>>().first().toUser()
        } else  {
            User()
        }
    } catch (e: Exception) {
        println("Exception getUserByEmail(): ${e.message}")
        User()
    }
}

suspend fun getUserById(id: Int): User {
    return try {
        val response = client.get("$BASE_URL/users") {
            parameter("id", "eq.$id")
        }
        if (response.status.value in 200..299) {
            response.body<UserDot>().toUser()
        } else  {
            User()
        }
    } catch (e: Exception) {
        println("Exception getUserById(): ${e.message}")
        User()
    }
}

suspend fun getVacancyById(id: Int): Vacancy {
    return try {
        val response = client.get("$BASE_URL/vacancies") {
            parameter("id", "eq.$id")
        }
        if (response.status.value in 200..299) {
            response.body<List<VacancyDto>>().first().toVacancy()
        } else  {
            VacancyDto().toVacancy()
        }
    } catch (e: Exception) {
        println("Exception getVacancyById(): ${e.message}")
        VacancyDto().toVacancy()
    }
}

suspend fun getUserFavList(userId: Int): List<Vacancy> {
    return try {
        val response = client.post("$BASE_URL/rpc/get_user_favorite_vacancies") {
            setBody(mapOf("user_id_param" to userId))
        }
        println("getUserFavList : ${response.bodyAsText()}")
        if (response.status.value in 200..299) {
            response.body<List<VacancyDto>>().map {
                it.toVacancy()
            }
        } else  {
           emptyList()
        }
    } catch (e: Exception) {
        println("Exception addToFav(): ${e.message}")
        emptyList()
    }
}

suspend fun addToFav(userId: Int, vacId: Int): Boolean {
    return try {
        val response = client.post("$BASE_URL/users_fav") {
            setBody(mapOf(
                "user_id" to userId,
                "vacanc_id" to vacId
            ))
        }
        println("addToFav : ${response.bodyAsText()}")
        if (response.status.value in 200..299) {
            true
        } else  {
           false
        }
    } catch (e: Exception) {
        println("Exception addToFav(): ${e.message}")
        false
    }
}

suspend fun deleteFromFav(userId: Int, vacId: Int): Boolean {
    return try {
        val response = client.delete("$BASE_URL/users_fav") {
            parameter("user_id", "eq.$userId")
            parameter("vacanc_id", "eq.$vacId")
        }
        println("deleteFromFav : ${response.bodyAsText()}")
        if (response.status.value in 200..299) {
            true
        } else  {
            false
        }
    } catch (e: Exception) {
        println("Exception deleteFromFav(): ${e.message}")
        false
    }
}

suspend fun addToVac(userId: Int, vacId: Int): Boolean {
    return try {
        val response = client.post("$BASE_URL/users_vac") {
            setBody(mapOf(
                "user_id" to userId,
                "vacanc_id" to vacId
            ))
        }
        if (response.status.value in 200..299) {
            true
        } else  {
            false
        }
    } catch (e: Exception) {
        println("Exception addToVac(): ${e.message}")
        false
    }
}

suspend fun deleteFromVac(userId: Int, vacId: Int): Boolean {
    return try {
        val response = client.delete("$BASE_URL/users_vac") {
            parameter("user_id", "eq.$userId")
            parameter("vacanc_id", "eq.$vacId")
        }
        println("deleteFromVac Response: ${response.bodyAsText()}")
        if (response.status.value in 200..299) {
            true
        } else {
            println("deleteFromVac Error: ${response.status.value}")
            false
        }
    } catch (e: Exception) {
        println("Exception deleteFromVac(): ${e.message}")
        false
    }
}

suspend fun getUserVacList(userId: Int): List<Vacancy> {
    return try {
        val response = client.post("$BASE_URL/rpc/get_user_response_vacancies") {
            setBody(mapOf("user_id_param" to userId))
        }
        println("getUserVacList : ${response.bodyAsText()}")
        if (response.status.value in 200..299) {
            response.body<List<VacancyDto>>().map { it.toVacancy() }
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        println("Exception getUserVacList(): ${e.message}")
        emptyList()
    }
}
