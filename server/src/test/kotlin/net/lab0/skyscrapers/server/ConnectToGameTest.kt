package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.server.dto.ConnectionResponse
import net.lab0.skyscrapers.server.dto.ErrorResponse
import net.lab0.skyscrapers.server.value.GameName
import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.format.KotlinxSerialization.auto
import org.junit.jupiter.api.Test

internal class ConnectToGameTest {
  @Test
  fun `can connect to a game`() {
    val service = ServiceImpl.new(mapOf(GameName("foo") to GameFactoryImpl().new()))
    val response = routed(service)(Request(POST, "/api/v1/games/foo/connect"))

    assertThat(response.status).isEqualTo(CREATED)
    val connection = Body.auto<ConnectionResponse>().toLens().extract(response)
    assertThat(connection.player).isEqualTo(0)
    assertThat(connection.token).isNotEmpty()
  }

  @Test
  fun `not found`() {
    val service = ServiceImpl.new()
    val response =
      routed(service)(Request(POST, "/api/v1/games/missing/connect"))
    assertThat(response.status).isEqualTo(NOT_FOUND)
  }

  @Test
  fun `refuse to connect if too many players`() {
    val service = ServiceImpl.new(mapOf(GameName("foo") to GameFactoryImpl().new(playerCount = 2)))
    val response0 = routed(service)(Request(POST, "/api/v1/games/foo/connect"))
    assertThat(response0.status).isEqualTo(CREATED)
    val response1 = routed(service)(Request(POST, "/api/v1/games/foo/connect"))
    assertThat(response1.status).isEqualTo(CREATED)

    val response3 = routed(service)(Request(POST, "/api/v1/games/foo/connect"))
    assertThat(response3.status).isEqualTo(FORBIDDEN)
    assertThat(
      Body.auto<ErrorResponse>().toLens().extract(response3)
    ).isEqualTo(
      ErrorResponse("The game foo is full.")
    )


    val connection = Body.auto<ConnectionResponse>().toLens().extract(response1)
    assertThat(connection.player).isEqualTo(1)
    assertThat(connection.token).isNotEmpty()
  }
}
