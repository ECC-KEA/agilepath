package dev.ecckea.agilepath.backend.support

import dev.ecckea.agilepath.backend.infrastructure.cache.TestCacheService
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class IntegrationTestBase {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var testCacheService: TestCacheService

    protected lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setUpClientAndSeedData() {
        webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build()
        testCacheService.clearAllCaches()
    }
}
