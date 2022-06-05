package net.lab0.skyscrapers.client.http

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.client.ServerIntegrationTest
import net.lab0.skyscrapers.server.ServiceImpl
import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.junit.jupiter.api.Test

internal class SkyscraperClientImplTest : FakeServerTest,
  ServerIntegrationTest {

  @Test
  fun `can connect to a real server`() {
    useServer { handler ->
      val client = SkyscraperClientImpl(handler)
      client.status().shouldBeRight()
    }
  }

  @Test
  fun `can connect to a server`() {
    fakeServer { handler ->
      val client = SkyscraperClientImpl(handler)
      client.status().shouldBeRight()
    }
  }

  @Test
  fun `fail to connect to something that is not a game server`() {
    // ssh port: nothing useful for us there
    val client = SkyscraperClientImpl(
      ClientFilters
        .SetBaseUriFrom(Uri.of("http://localhost:22/api/v1/"))
        .then(OkHttp())
    )
    client.status().shouldBeLeft()
  }

  @Test
  fun `can show a game`() {
    fakeServer { handler ->
      val client = SkyscraperClientImpl(handler)
      val foo = GameName("foo")
      client.create(foo).shouldBeRight()
      client.join(foo).shouldBeRight()
      val state = client.state(foo).shouldBeRight()
      state shouldNotBe null
    }
  }

  @Test
  fun `can list games on the server`() {
    fakeServer { handler ->
      val client = SkyscraperClientImpl(handler)
      val list = client.listGames()
      list shouldBe listOf()
    }
  }

  @Test
  fun `can create a game`() {
    val service = ServiceImpl.new()

    fakeServer(service = service) { handler ->
      val client = SkyscraperClientImpl(handler)
      val gameName = "fus ro dah"
      val fusRoDah = GameName(gameName)
      client.create(fusRoDah).shouldBeRight()

      client.listGames() shouldContain fusRoDah
    }
  }

  @Test
  fun `can't create a game with the same name`() {
    val service = ServiceImpl.new()

    fakeServer(service = service) { handler ->
      val client = SkyscraperClientImpl(handler)
      val gameName = "fus ro dah"
      val fusRoDah = GameName(gameName)

      client.create(fusRoDah).shouldBeRight()
      client.create(fusRoDah).shouldBeLeft()

      client.listGames() shouldContain fusRoDah
    }
  }

  @Test
  fun `can place a builder`() {
    val service = ServiceImpl.new()
    val gameName = "go"
    val cnc = GameName(gameName)
    service.createGame(cnc)
    val p0 = service.join(cnc)
    val p1 = service.join(cnc)

    fakeServer(service = service) { handler ->
      val client = SkyscraperClientImpl(handler)

      val firstBuilder = Position(0, 0)
      client.place(cnc, p0.token, firstBuilder).shouldBeRight()

      val state = client.state(cnc).shouldBeRight()
      state.getBuilders(p0.id) shouldBe listOf(firstBuilder)
    }
  }
}
