package com.example.kal.db

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

const val BASE_URL = "https://csfoequtwhillchrvwoi.supabase.co/rest/v1"
const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNzZm9lcXV0d2hpbGxjaHJ2d29pIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU0ODY1NDQsImV4cCI6MjA2MTA2MjU0NH0.pSVWx_UPDo23KARuTUzoXbcAPj38h0FJIIPOjEwrPiA"

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
    defaultRequest {
        url(BASE_URL)
        headers.append("apikey", API_KEY)
        headers.append("Authorization", "Bearer $API_KEY")
        headers.append("Content-Type", "application/json")
    }
}