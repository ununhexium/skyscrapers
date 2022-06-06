package net.lab0.skyscrapers.server

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.merge
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.BuildTurnDTO
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameStateDTO
import net.lab0.skyscrapers.api.dto.GameViolationDTO
import net.lab0.skyscrapers.api.dto.GameViolationsDTO
import net.lab0.skyscrapers.api.dto.PlaceTurnDTO
import net.lab0.skyscrapers.api.dto.SealTurnDTO
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.http4k.AUTHORIZATION
import net.lab0.skyscrapers.api.structure.ErrorMessage
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.exception.GameRuleViolationException
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Header
import org.http4k.lens.LensFailure
import org.http4k.routing.path


fun Request.pathGameName(): Either<Response, GameName> =
  this.path("gameName")?.let(::GameName)?.let { Right(it) }
    ?: Left(notFound("No game name found in the URL path"))

fun notFound(vararg messages: String) =
  Response(NOT_FOUND).with(
    Body.auto<ErrorResponse>().toLens() of
        ErrorResponse(messages.toList())
  )

fun notFound(message: ErrorMessage) =
  notFound(message.value)

fun badRequest(vararg messages: String) =
  Response(BAD_REQUEST).with(
    Body.auto<ErrorResponse>().toLens() of
        ErrorResponse(messages.toList())
  )

fun forbidden(vararg messages: String) =
  Response(Status.FORBIDDEN).with(
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

fun PlaceTurnDTO.toModel(
  game: GameName,
  token: AccessToken,
  service: Service
): TurnType? {
  return service.getPlayerId(game, token)?.let { playerId ->
    TurnType.PlacementTurn(playerId, position.toModel())
  }
}

fun BuildTurnDTO.toModel(
  game: GameName,
  token: AccessToken,
  service: Service
): TurnType? {
  return service.getPlayerId(game, token)?.let { playerId ->
    TurnType.MoveTurn.BuildTurn(
      playerId,
      start.toModel(),
      target.toModel(),
      build.toModel()
    )
  }
}

fun SealTurnDTO.toModel(game: GameName, accessToken: AccessToken, service: Service): TurnType? {
  return service.getPlayerId(game, accessToken)?.let { playerId ->
    TurnType.MoveTurn.BuildTurn(
      playerId,
      start.toModel(),
      target.toModel(),
      seal.toModel()
    )
  }
}


fun withGameName(
  req: Request,
  f: (GameName) -> Response
): Response =
  req.pathGameName().map(f).merge()

fun withToken(
  req: Request,
  f: (AccessToken) -> Response
): Response =
  try {
    val bearer = Header.AUTHORIZATION.extract(req)
    f(bearer.token)
  } catch (e: LensFailure) {
    unauthorized(
      "Can't access this game: give a Authorization: Bearer ... " +
          "header that you got when connecting to access the game."
    )
  }

fun withGame(
  req: Request,
  service: Service,
  f: (Pair<GameName, Game>) -> Response
): Response =
  withGameName(req) { gameName ->
    service.getGame(gameName).mapLeft { notFound(it) }.map { game ->
      f(gameName to game)
    }.merge()
  }

fun playTurn(
  turn: TurnType,
  game: Game
) = try {
  game.play(turn)

  Response(Status.CREATED).with(
    Body.auto<GameStateDTO>().toLens() of GameStateDTO(game.state)
  )
} catch (e: GameRuleViolationException) {
  Response(Status.CONFLICT).with(
    Body.auto<GameViolationsDTO>().toLens() of GameViolationsDTO(
      e.violations.map { GameViolationDTO(it) }
    )
  )
}

