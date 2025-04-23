package dev.ecckea.agilepath.backend.shared.security

import com.fasterxml.jackson.databind.ObjectMapper
import dev.ecckea.agilepath.backend.shared.dto.ErrorResponse
import dev.ecckea.agilepath.backend.shared.utils.nowInZone
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val error = ErrorResponse(
            status = 401,
            message = "Unauthorized – Invalid or missing token",
            timestamp = nowInZone()
        )

        response.writer.write(objectMapper.writeValueAsString(error))
    }
}
