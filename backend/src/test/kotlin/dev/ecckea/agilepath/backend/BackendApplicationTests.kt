package dev.ecckea.agilepath.backend

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = ["CLERK_ISSUER=https://fake-issuer.dev"])
class BackendApplicationTests {

    @Test
    fun contextLoads() {
    }

}
