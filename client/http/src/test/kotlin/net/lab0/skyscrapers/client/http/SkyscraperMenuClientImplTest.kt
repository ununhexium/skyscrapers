package net.lab0.skyscrapers.client.http

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.client.ServerIntegrationTest
import org.http4k.client.OkHttp
import org.junit.jupiter.api.Test

internal class SkyscraperMenuClientImplTest : net.lab0.skyscrapers.client.ServerIntegrationTest {
  @Test
  fun `can list games on the server`() {
    useServer { url ->
      val client = SkyscraperClientImpl(OkHttp())
      val menu = client.connect(url).shouldBeRight()

      val list = menu.listGames()
      list shouldBe listOf()
      Either.Right(list)
    }
  }
}