package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldNotBe
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.client.OkHttp
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SkyscraperGameClientImplTest: ServerIntegrationTest {
  val client = SkyscraperClientImpl(OkHttp())

  @Disabled // TODO continue
  @Test
  fun `can show a game`() {
    useServer { url ->
      val menu = client.status(url).shouldBeRight()
      val foo = GameName("foo")
      menu.create(foo).shouldBeRight()
      val game = menu.join(foo).shouldBeRight()
      val state = game.state().shouldBeRight()
      state shouldNotBe null
    }
  }
}