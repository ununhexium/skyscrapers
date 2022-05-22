package net.lab0.skyscrapers.client.clikt.command

import io.kotest.assertions.arrow.core.shouldBeRight
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.client.OkHttp
import org.junit.jupiter.api.Test

internal class NewGameTest : ServerIntegrationTest {
  @Test
  fun `can create a new game`() {
    useServer { url ->
      val client = SkyscraperClientImpl(OkHttp())
      val menu = client.status(url).shouldBeRight()

      val cli =
      menu.create(GameName("Springfield")).shouldBeRight()
    }
  }
}