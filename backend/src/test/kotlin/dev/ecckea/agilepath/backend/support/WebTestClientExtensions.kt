package dev.ecckea.agilepath.backend.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

val json: ObjectMapper = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

inline fun <reified T> WebTestClient.ResponseSpec.parseBody(): T {
    val result = this.expectBody().returnResult()
    val bytes = result.responseBody
        ?: error("Expected response body but got null. Status: ${result.status}")
    return json.readValue(bytes)
}

inline fun <reified T> WebTestClient.ResponseSpec.parseListBody(): List<T> {
    val result = this.expectBody().returnResult()
    val bytes = result.responseBody
        ?: error("\"Expected response body but got null. Status: ${result.status}\"")
    return try {
        json.readValue(bytes)
    } catch (e: Exception) {
        // If standard parsing fails, try to handle the special format
        val content = String(bytes)
        if (content.contains("[\"java.util.ArrayList\",[")) {
            // Extract the actual array part
            val startIndex = content.indexOf(",[") + 1
            val endIndex = content.lastIndexOf("]")
            val arrayContent = content.substring(startIndex, endIndex + 1)
            json.readValue(arrayContent.toByteArray())
        } else {
            throw e
        }
    }
}

inline fun <reified T> WebTestClient.ResponseSpec.parseBodyOrNull(): T? {
    val bytes = this.expectBody().returnResult().responseBody
        ?: return null
    return json.readValue(bytes)
}

inline fun <reified T> WebTestClient.ResponseSpec.parseListBodyOrNull(): List<T>? {
    val bytes = this.expectBody().returnResult().responseBody
        ?: return null
    return json.readValue(bytes)
}

fun WebTestClient.webGetWithAuth(uri: String, token: String = "test-token") = this
    .get()
    .uri(uri)
    .accept(MediaType.APPLICATION_JSON)
    .header("Authorization", "Bearer $token")

fun WebTestClient.webGet(uri: String) = this
    .get()
    .uri(uri)
    .accept(MediaType.APPLICATION_JSON)

fun WebTestClient.webPostWithAuth(uri: String, body: Any, token: String = "test-token") = this
    .post()
    .uri(uri)
    .header("Authorization", "Bearer $token")
    .contentType(MediaType.APPLICATION_JSON)
    .accept(MediaType.APPLICATION_JSON)
    .bodyValue(body)

fun WebTestClient.webPost(uri: String, body: Any) = this
    .post()
    .uri(uri)
    .contentType(MediaType.APPLICATION_JSON)
    .accept(MediaType.APPLICATION_JSON)
    .bodyValue(body)

fun WebTestClient.webPutWithAuth(uri: String, body: Any, token: String = "test-token") = this
    .put()
    .uri(uri)
    .header("Authorization", "Bearer $token")
    .contentType(MediaType.APPLICATION_JSON)
    .accept(MediaType.APPLICATION_JSON)
    .bodyValue(body)

fun WebTestClient.webPatchWithAuth(uri: String, body: Any? = null, token: String = "test-token") = this
    .patch()
    .uri(uri)
    .header("Authorization", "Bearer $token")
    .contentType(MediaType.APPLICATION_JSON)
    .accept(MediaType.APPLICATION_JSON)
    .let { if (body != null) it.bodyValue(body) else it }

fun WebTestClient.webPut(uri: String, body: Any) = this
    .put()
    .uri(uri)
    .contentType(MediaType.APPLICATION_JSON)
    .accept(MediaType.APPLICATION_JSON)
    .bodyValue(body)

fun WebTestClient.webDeleteWithAuth(uri: String, token: String = "test-token") = this
    .delete()
    .uri(uri)
    .header("Authorization", "Bearer $token")
    .accept(MediaType.APPLICATION_JSON)

fun WebTestClient.webDelete(uri: String) = this
    .delete()
    .uri(uri)
    .accept(MediaType.APPLICATION_JSON)