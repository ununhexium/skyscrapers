package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.server.dto.GameError
import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class NewGameTest {
  @Test
  fun `creates a new game when requesting new game`() {
    val games = mutableMapOf<String, Game>()

    val gameName = "fubar"
    val created = routed(games)(Request(POST, "/api/v1/games/$gameName"))
    val game = games[gameName]

    assertThat(game).isNotNull
    assertThat(created.status).isEqualTo(Status.CREATED)
    // check that it uses the default parameters
    assertThat(
      Body.auto<GameResponse>().toLens().extract(created)
    ).isEqualTo(
      GameResponse(gameName, Game.new().state)
    )

    // now the game already exists
    val alreadyExists = routed(games)(Request(POST, "/api/v1/games/$gameName"))
    assertThat(
      Body.auto<GameError>().toLens()[alreadyExists]
    ).isEqualTo(
      GameError("The game $gameName already exists.")
    )
  }
}
