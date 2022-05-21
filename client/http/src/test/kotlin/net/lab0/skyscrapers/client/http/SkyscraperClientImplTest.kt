package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import net.lab0.skyscrapers.client.ServerIntegrationTest
import org.http4k.client.OkHttp
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class SkyscraperClientImplTest : ServerIntegrationTest {
  private val randomPort = Random.nextInt(20_000, 30_000)

  @Test
  fun `can connect to a server`() {
    useServer(randomPort) { url ->
      val client = SkyscraperClientImpl(OkHttp())

      client.connect(url).shouldBeRight()

      // ssh port: nothing useful for us there
      client.connect("http://localhost:22/api/").shouldBeLeft()
    }
  }
}