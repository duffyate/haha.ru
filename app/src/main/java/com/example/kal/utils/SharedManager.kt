package com.example.kal.utils

import android.content.Context
import androidx.core.content.edit

class SharedManager(private val context: Context) {
    private val shared = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    // Saving user data
    fun saveLocalUserId(id: Int) {
        shared.edit {
            putInt("user_id", id)
        }
    }

    fun saveLocalFirstName(firstName: String) {
        shared.edit {
            putString("first_name", firstName)
        }
    }

    fun saveLocalLastName(lastName: String) {
        shared.edit {
            putString("last_name", lastName)
        }
    }

    fun saveLocalAddress(address: String) {
        shared.edit {
            putString("address", address)
        }
    }

    // Getting user data
    fun getLocalUserId(): Int {
        return shared.getInt("user_id", -1)
    }

    fun getLocalFirstName(): String? {
        return shared.getString("first_name", null)
    }

    fun getLocalLastName(): String? {
        return shared.getString("last_name", null)
    }

    fun getLocalAddress(): String? {
        return shared.getString("address", null)
    }

    // Clearing user data
    fun clearUser() {
        shared.edit {
            remove("user_id")
            remove("first_name")
            remove("last_name")
            remove("address")
        }
    }
}
