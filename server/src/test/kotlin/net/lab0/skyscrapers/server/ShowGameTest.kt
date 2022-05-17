package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.server.dto.GameError
import net.lab0.skyscrapers.server.value.GameName
import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class ShowGameTest {
  @Test
  fun `can show a game`() {
    val newGame = Game.new()
    val service = ServiceImpl.new(mapOf(GameName("foo") to newGame))

    val readGame = routed(service)(Request(GET, "/api/v1/games/foo"))

    assertThat(readGame.status).isEqualTo(OK)
    assertThat(
      Body.auto<GameResponse>().toLens().extract(readGame)
    ).isEqualTo(GameResponse(GameName("foo"), newGame.state))
  }

  @Test
  fun `return not found when there is no such game`() {
    val readGame = routed(ServiceImpl.new())(Request(GET, "/api/v1/games/missing"))

    assertThat(readGame.status).isEqualTo(NOT_FOUND)
    assertThat(
      Body.auto<GameError>().toLens().extract(readGame)
    ).isEqualTo(GameError("No game named 'missing'"))
  }
}
