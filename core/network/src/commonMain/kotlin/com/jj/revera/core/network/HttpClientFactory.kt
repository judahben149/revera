package com.jj.revera.core.network

import com.jj.revera.core.common.AppEnvironment
import com.jj.revera.core.common.BuildConfig
import com.jj.revera.core.common.logging.logger
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger as KtorLogger

/** Shared JSON configuration: tolerant of new server fields, strict about types. */
val ReveraJson: Json =
    Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

/**
 * Builds the app-wide [HttpClient]. Pass an [engine] in tests (e.g. `MockEngine`); in production
 * the platform engine (OkHttp, Darwin, Fetch) is resolved from the module's dependencies.
 */
fun createHttpClient(
    engine: HttpClientEngine? = null,
    json: Json = ReveraJson,
    baseUrl: String = BuildConfig.API_BASE_URL,
    environment: AppEnvironment = AppEnvironment.current,
): HttpClient {
    val config: HttpClientConfig<*>.() -> Unit = {
        expectSuccess = true
        install(ContentNegotiation) { json(json) }
        install(HttpTimeout) {
            connectTimeoutMillis = 15_000
            requestTimeoutMillis = 30_000
        }
        install(Logging) {
            val log = logger("Http")
            logger =
                object : KtorLogger {
                    override fun log(message: String) = log.d { message }
                }
            level = if (environment == AppEnvironment.Prod) LogLevel.NONE else LogLevel.INFO
            sanitizeHeader { it == HttpHeaders.Authorization }
        }
        defaultRequest {
            if (baseUrl.isNotBlank()) url(baseUrl)
            contentType(ContentType.Application.Json)
        }
    }
    return if (engine != null) HttpClient(engine, config) else HttpClient(config)
}
