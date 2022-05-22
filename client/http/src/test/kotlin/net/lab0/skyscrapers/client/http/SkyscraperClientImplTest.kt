package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import net.lab0.skyscrapers.client.ServerIntegrationTest
import org.http4k.client.OkHttp
import org.junit.jupiter.api.Test

internal class SkyscraperClientImplTest : ServerIntegrationTest {
  @Test
  fun `can connect to a server`() {
    useServer { url ->
      val client = SkyscraperClientImpl(OkHttp())

      client.connect(url).shouldBeRight()

      // ssh port: nothing useful for us there
      client.connect("http://localhost:22/api/").shouldBeLeft()
    }
  }
}