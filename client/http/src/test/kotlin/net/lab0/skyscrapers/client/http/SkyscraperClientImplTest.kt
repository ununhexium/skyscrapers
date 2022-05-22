package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SkyscraperClientImplTest : ServerIntegrationTest {
  val client = SkyscraperClientImpl(OkHttp())

  @Test
  fun `can connect to a server`() {
    useServer { url ->
      client.status(url).shouldBeRight()

      // ssh port: nothing useful for us there
      client.status("http://localhost:22/api/").shouldBeLeft()
    }
  }

  @Disabled // TODO continue
  @Test
  fun `can show a game`() {
    useServer { url ->
      val foo = GameName("foo")
      client.create(url, foo).shouldBeRight()
      client.join(url, foo).shouldBeRight()
      val state = client.state(url, foo).shouldBeRight()
      state shouldNotBe null
    }
  }

  @Test
  fun `can list games on the server`() {
    useServer { url ->
      val list = client.listGames(url)
      list shouldBe listOf()
    }
  }

  @Test
  fun `can create a game`() {
    val service = ServiceImpl.new()

    useServer(service = service) { url ->
      val gameName = "fus ro dah"
      val fusRoDah = GameName(gameName)
      client.create(url, fusRoDah).shouldBeRight()

      client.listGames(url) shouldContain fusRoDah
    }
  }

  @Test
  fun `can't create a game with the same name`() {
    val service = ServiceImpl.new()

    useServer(service = service) { url ->
      val gameName = "fus ro dah"
      val fusRoDah = GameName(gameName)

      client.create(url, fusRoDah).shouldBeRight()
      client.create(url, fusRoDah).shouldBeLeft()

      client.listGames(url) shouldContain fusRoDah
    }
  }
}