package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.api.dto.BuildTurnDTO
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.PlaceTurnDTO
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.TurnType
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.path


fun Request.pathGameName(): GameName? {
  val path = this.path("gameName")
  val asGame = path?.let(::GameName)
  return asGame
}

fun notFound(vararg messages: String) =
  Response(NOT_FOUND).with(
    Body.auto<ErrorResponse>().toLens() of
        ErrorResponse(messages.toList())
  )

fun badRequest(vararg messages: String) =
  Response(BAD_REQUEST).with(
    Body.auto<ErrorResponse>().toLens() of
        ErrorResponse(messages.toList())
  )

fun unauthorized(vararg messages: String?) =
  Response(UNAUTHORIZED).with(
    Body.auto<ErrorResponse>().toLens() of
        ErrorResponse(messages.filterNotNull())
  )

fun internalServerError(vararg messages: String?) =
  Response(INTERNAL_SERVER_ERROR).with(
    Body.auto<ErrorResponse>().toLens() of
        ErrorResponse(messages.filterNotNull())
  )

fun PlaceTurnDTO.toModel(game: GameName, service: Service): TurnType? {
  return service.getPlayerId(game, this.player)?.let {
    TurnType.PlacementTurn(it, position.toModel())
  }
}

fun BuildTurnDTO.toModel(game: GameName, service: Service): TurnType? {
  return service.getPlayerId(game, this.player)?.let {
    TurnType.MoveTurn.BuildTurn(
      it,
      start.toModel(),
      target.toModel(),
      build.toModel()
    )
  }
}
