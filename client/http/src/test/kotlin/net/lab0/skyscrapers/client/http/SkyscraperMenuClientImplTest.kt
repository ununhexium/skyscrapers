package net.lab0.skyscrapers.client.http

import arrow.core.Either
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.client.OkHttp
import org.junit.jupiter.api.Test

internal class SkyscraperMenuClientImplTest : net.lab0.skyscrapers.client.ServerIntegrationTest {
  val client = SkyscraperClientImpl(OkHttp())

  @Test
  fun `can list games on the server`() {
    useServer { url ->
      val menu = client.connect(url).shouldBeRight()

      val list = menu.listGames()
      list shouldBe listOf()
    }
  }

  @Test
  fun `can create a game`() {
    val service = ServiceImpl.new()

    useServer(service = service) { url ->
      val menu = client.connect(url).shouldBeRight()
      val gameName = "fus ro dah"
      val fusRoDah = GameName(gameName)
      menu.create(fusRoDah).shouldBeRight()

      menu.listGames() shouldContain fusRoDah
    }
  }

  @Test
  fun `can't create a game with the same name`() {
    val service = ServiceImpl.new()

    useServer(service = service) { url ->
      val menu = client.connect(url).shouldBeRight()
      val gameName = "fus ro dah"
      val fusRoDah = GameName(gameName)

      menu.create(fusRoDah).shouldBeRight()
      menu.create(fusRoDah).shouldBeLeft()

      menu.listGames() shouldContain fusRoDah
    }
  }
}