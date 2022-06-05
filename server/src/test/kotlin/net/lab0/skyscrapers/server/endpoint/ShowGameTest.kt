package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.routed
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
    val newGame = GameFactoryImpl().new()
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
      Body.auto<ErrorResponse>().toLens().extract(readGame)
    ).isEqualTo(ErrorResponse("No game named 'missing'"))
  }
}
