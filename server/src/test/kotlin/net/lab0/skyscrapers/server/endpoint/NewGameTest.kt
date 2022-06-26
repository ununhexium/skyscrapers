package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.routed
import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class NewGameTest {
  @Test
  fun `creates a new game when requesting new game`() {
    val service = ServiceImpl.new()

    val gameName = GameName("fubar")
    val created = routed(service)(Request(POST, "/api/v1/games/$gameName"))
    val game = service.getGame(gameName)

    assertThat(game).isNotNull
    assertThat(created.status).isEqualTo(Status.CREATED)
    // check that it uses the default parameters
    assertThat(
      Body.auto<GameResponse>().toLens().extract(created)
    ).isEqualTo(
      GameResponse(gameName, GameFactoryImpl().new().state)
    )

    // now the game already exists
    val alreadyExists = routed(service)(Request(POST, "/api/v1/games/$gameName"))
    assertThat(
      Body.auto<ErrorResponse>().toLens()[alreadyExists]
    ).isEqualTo(
      ErrorResponse("The game $gameName already exists.")
    )
  }
}
