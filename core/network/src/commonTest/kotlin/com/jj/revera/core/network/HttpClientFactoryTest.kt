package com.jj.revera.core.network

import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class HttpClientFactoryTest {
    @Serializable
    private data class Greeting(
        val message: String,
    )

    @Test
    fun resolvesRelativeUrlsAgainstBaseUrlAndDecodesJson() =
        runTest {
            val engine =
                MockEngine { request ->
                    assertEquals("https://api.test/v1/greeting", request.url.toString())
                    respond(
                        content = """{"message":"hello","ignored":true}""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }
            val client = createHttpClient(engine = engine, baseUrl = "https://api.test/")

            val greeting: Greeting = client.get("v1/greeting").body()

            assertEquals(Greeting("hello"), greeting)
        }
}
