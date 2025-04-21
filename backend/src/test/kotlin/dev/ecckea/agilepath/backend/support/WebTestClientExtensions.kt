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
    return json.readValue(bytes)
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

fun WebTestClient.webPost(uri: String, body: Any) = this
    .post()
    .uri(uri)
    .contentType(MediaType.APPLICATION_JSON)
    .accept(MediaType.APPLICATION_JSON)
    .bodyValue(body)

fun WebTestClient.webPut(uri: String, body: Any) = this
    .put()
    .uri(uri)
    .contentType(MediaType.APPLICATION_JSON)
    .accept(MediaType.APPLICATION_JSON)
    .bodyValue(body)

fun WebTestClient.webDelete(uri: String) = this
    .delete()
    .uri(uri)
    .accept(MediaType.APPLICATION_JSON)