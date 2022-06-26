package net.lab0.skyscrapers.server.endpoint

import arrow.core.Either
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.server.JoiningError
import net.lab0.skyscrapers.server.PlayerAndToken
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.endpoint.JoinGame.Examples.joiningImpossibleTooManyPlayers
import net.lab0.skyscrapers.server.routed
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class JoinGameTest {
  @Test
  fun `can connect to a game`() {
    val exampleBody = JoinGame.Examples.exampleBody

    val service = mockk<Service> {
      every { join(GameName("foo")) } returns
          Either.Right(PlayerAndToken(exampleBody.player, exampleBody.token))
    }
    val response = routed(service)(Request(POST, "/api/v1/games/foo/join"))

    response.status shouldBe JoinGame.Examples.exampleResponse.status

    val connection = Body.auto<ConnectionResponse>().toLens().extract(response)
    connection.player shouldBe exampleBody.player
    connection.token shouldBe exampleBody.token
  }

  @Test
  fun `not found`() {
    val service = ServiceImpl.new()
    val response = routed(service)(Request(POST, "/api/v1/games/missing/join"))
    response.status shouldBe JoinGame.Examples.joiningImpossibleWhenInvalidGameName.status
  }

  @Test
  fun `refuse to connect if the game is full`() {
    val service = mockk<Service> {
      every { join(GameName("foo")) } returns
          Either.Left(JoiningError.GameIsFull)
    }

    val response = routed(service)(Request(POST, "/api/v1/games/foo/join"))
    val example = joiningImpossibleTooManyPlayers
    response.status shouldBe example.status
    response.body shouldBe example.body
  }
}
