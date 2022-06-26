package net.lab0.skyscrapers.client.http

import arrow.core.Either
import arrow.core.Either.Right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.FakeServerTest
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.served
import org.http4k.client.OkHttp
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class SkyscraperClientImplTest : FakeServerTest {

  @Test
  fun `can connect to a real server`() {
    val service = ServiceImpl.new()
    val port = Random.nextInt(10_000, 20_000)
    val config = Undertow(port)
    val server = served(service).asServer(config).start()

    try {
      val url = "http://localhost:$port/"
      val handler = ClientFilters.SetBaseUriFrom(Uri.of(url)).then(OkHttp())

      val client = SkyscraperClientImpl(handler)
      client.status().shouldBeRight()
    } finally {
      server.stop()
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
        .SetBaseUriFrom(Uri.of("http://example.com/"))
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
    val p0 = service.join(cnc).shouldBeRight()
    service.join(cnc).shouldBeRight()

    fakeServer(service = service) { handler ->
      val client = SkyscraperClientImpl(handler)

      val firstBuilder = Position(0, 0)
      client.place(cnc, p0.token, firstBuilder).shouldBeRight()

      val state = client.state(cnc).shouldBeRight()
      state.getBuilders(p0.id) shouldBe listOf(firstBuilder)
    }
  }

  @Test
  fun `can't place a builder where there is another one`() {
    val service = ServiceImpl.new()
    val gameName = "go"
    val cnc = GameName(gameName)
    service.createGame(cnc)
    val p0 = service.join(cnc).shouldBeRight()
    val p1 = service.join(cnc).shouldBeRight()

    fakeServer(service = service) { handler ->
      val client = SkyscraperClientImpl(handler)

      val firstBuilder = Position(0, 0)
      client.place(cnc, p0.token, firstBuilder).shouldBeRight()
      client.place(cnc, p1.token, firstBuilder).shouldBeLeft()

      val state = client.state(cnc).shouldBeRight()
      state.getBuilders(p1.id) shouldBe emptyList()
    }
  }

  @Test
  fun `can get the history of a game`() {
    val foo = GameName("foo")
    val state = GameState.DUMMY
    val service = mockk<Service>() {
      every { getGameHistory(foo) } returns Right(listOf(state))
    }

    fakeServer(service = service) { handler ->
      val client = SkyscraperClientImpl(handler)
      client.history(foo) shouldBeRight listOf(state)
    }
  }
}
