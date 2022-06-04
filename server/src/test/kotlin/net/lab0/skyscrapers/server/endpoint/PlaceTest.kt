package net.lab0.skyscrapers.server.endpoint

import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameStateDTO
import net.lab0.skyscrapers.api.dto.GameViolationsDTO
import net.lab0.skyscrapers.api.dto.PlaceTurnDTO
import net.lab0.skyscrapers.api.dto.PositionDTO
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.server.Authorization
import net.lab0.skyscrapers.server.ServiceImpl
import net.lab0.skyscrapers.server.AUTHORIZATION
import net.lab0.skyscrapers.server.routed
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.FORBIDDEN
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Header
import org.junit.jupiter.api.Test

internal class PlaceTest {

  @Test
  fun `can place a builder`() {
    val gameName = GameName("foo")
    val service = ServiceImpl.new(mapOf(gameName to GameFactoryImpl().new()))
    val player0 = service.join(gameName)
    val player1 = service.join(gameName)

    val pos = PositionDTO(0, 0)
    val req = Request(POST, "/api/v1/games/foo/place").with(
      Body.auto<PlaceTurnDTO>().toLens() of
          PlaceTurnDTO(0, pos),
      Header.AUTHORIZATION of Authorization.Bearer(player0.token),
    )
    val response = routed(service)(req)

    response.status shouldBe CREATED

    val state = Body.auto<GameStateDTO>().toLens().extract(response)
    state.currentPlayer shouldBe 1
    state.builders.toModel()[pos.toModel()] shouldBe 0
  }

  @Test
  fun `only allow an authorized player to place a builder`() {
    val gameName = GameName("foo")
    val service = ServiceImpl.new(mapOf(gameName to GameFactoryImpl().new()))
    val player0 = service.join(gameName)
    val player1 = service.join(gameName)

    val pos = PositionDTO(0, 0)
    val response = routed(service)(
      Request(POST, "/api/v1/games/foo/place").with(
        Body.auto<PlaceTurnDTO>().toLens() of
            PlaceTurnDTO(0, pos)
      )
    )

    response.status shouldBe UNAUTHORIZED

    val state = Body.auto<ErrorResponse>().toLens().extract(response)
    state.errors shouldBe listOf(
      "Can't access this game: give a Authorization: Bearer ... " +
          "header that you got when connecting to access the game."
    )
  }

  // TODO: should be extracted to GameAccessTest
  @Test
  fun `tell when a rule is violated, wrong turn`() {
    val gameName = GameName("foo")
    val service = ServiceImpl.new(mapOf(gameName to GameFactoryImpl().new()))
    val player0 = service.join(gameName)
    val player1 = service.join(gameName)

    val pos = PositionDTO(0, 0)
    val response = routed(service)(
      Request(POST, "/api/v1/games/foo/place").with(
        Body.auto<PlaceTurnDTO>().toLens() of
            PlaceTurnDTO(1, pos),
        Header.AUTHORIZATION of Authorization.Bearer(player1.token)
      )
    )

    response.status shouldBe CONFLICT

    val state = Body.auto<GameViolationsDTO>().toLens().extract(response)
    state.violations shouldHaveSize 1
  }
}