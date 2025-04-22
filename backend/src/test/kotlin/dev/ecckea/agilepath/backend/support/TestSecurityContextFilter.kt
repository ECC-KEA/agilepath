package dev.ecckea.agilepath.backend.support

import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtDecoder
import java.io.IOException

class TestSecurityContextFilter(
    private val jwtDecoder: JwtDecoder
) : Filter, Logged() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val authHeader = httpRequest.getHeader("Authorization")

        if (authHeader?.startsWith("Bearer ") == true) {
            val token = authHeader.removePrefix("Bearer ").trim()
            try {
                val jwt = jwtDecoder.decode(token)
                val principal = UserPrincipal(
                    id = jwt.subject,
                    email = jwt.getClaimAsString("email"),
                    githubUsername = jwt.getClaimAsString("username"),
                    fullName = jwt.getClaimAsString("name"),
                    avatarUrl = jwt.getClaimAsString("image_url")
                )
                val auth = UsernamePasswordAuthenticationToken(principal, jwt.tokenValue, emptyList())

                // Set the authentication in the current security context
                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = auth
                SecurityContextHolder.setContext(context)

                log.debug("Set authentication in security context for user: ${principal.id}")
            } catch (e: Exception) {
                log.warn("Invalid test token: ${e.message}")
            }
        }

        chain.doFilter(request, response)
    }
}
