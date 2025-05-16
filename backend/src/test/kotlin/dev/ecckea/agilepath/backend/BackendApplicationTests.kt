package dev.ecckea.agilepath.backend

import dev.ecckea.agilepath.backend.config.TestAppConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAppConfig::class)
@TestPropertySource(properties = ["CLERK_ISSUER=https://fake-issuer.dev"])
class BackendApplicationTests {

    @Test
    fun contextLoads() {
    }

}
