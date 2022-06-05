package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.ListGamesResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.routed
import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Body
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class ListGamesTest {
  @Test
  fun `can list all the games`() {
    val service = ServiceImpl.new()
    service.createGame(GameName("foo"))
    service.createGame(GameName("bar"))

    val readGame = routed(service)(Request(GET, "/api/v1/games/"))

    assertThat(readGame.status).isEqualTo(OK)
    assertThat(
      Body.auto<ListGamesResponse>().toLens().extract(readGame)
    ).isEqualTo(
      ListGamesResponse(listOf("foo", "bar"))
    )
  }
}
