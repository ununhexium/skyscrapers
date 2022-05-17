package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.server.dto.PlaceTurnDTO
import net.lab0.skyscrapers.server.dto.PositionDTO
import net.lab0.skyscrapers.server.dto.TurnTypeDTO
import net.lab0.skyscrapers.server.value.GameName
import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class PlayGameTest {

  @Test
  fun `can place a builder`() {
    val gameName = GameName("foo")
    val service = ServiceImpl.new(mapOf(gameName to Game.new()))
    val player0 = service.connect(gameName)
    val player1 = service.connect(gameName)

    val response = routed(service)(
      Request(POST, "/api/v1/games/foo/play").with(
        Body.auto<TurnTypeDTO>().toLens() of
            TurnTypeDTO.place(PlaceTurnDTO(0, PositionDTO(0, 0)))
      )
    )

    assertThat(response.status).isEqualTo(Status.OK)
    val gameResponse = Body.auto<GameResponse>().toLens().extract(response)
    assertThat(gameResponse.state.currentPlayer).isEqualTo(1)
  }

  // TODO: test that can start playing only after all the players have joined
  // TODO: test for authorization: only the play whose turn it is to play can play
  // TODO: test for bad request, missing body, missing auth, wrong player turn etc.
}
