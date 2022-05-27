package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.dto.ConnectionResponse
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.api.dto.ListGamesResponse
import net.lab0.skyscrapers.api.dto.StatusResponse
import net.lab0.skyscrapers.api.dto.value.GameName
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.format.KotlinxSerialization.auto

/**
 * @param handler that targets the server (no /api/v1/...)
 */
class SkyscraperClientImpl(
  val handler: HttpHandler,
) : SkyscraperClient {
  override fun status(): Either<Status, StatusResponse> {
    val req = Request(Method.GET, "/api/v1/status")
    val res = handler(req)
    return if (res.status == Status.OK) {
      Either.Right(res.extract())
    } else {
      Either.Left(res.status)
    }
  }

  override fun state(name: GameName): Either<Errors, GameState> {
    val req = Request(Method.GET, "/api/v1/games" / name)
    val res = handler(req)

    if (res.status == Status.OK) {
      return Either.Right(res.extract<GameResponse>().state.toState())
    } else {
      return Either.Left(res.extract<ErrorResponse>().errors)
    }
  }

  override fun play(turn: TurnType): Either<Errors, GameState> {
    TODO("Not yet implemented")
  }

  override fun listGames(): List<GameName> {
    val req = Request(Method.GET, "/api/v1/games")
    val res = handler(req)

    val list = Body.auto<ListGamesResponse>().toLens()(res)
    return list.names.map { GameName(it) }
  }

  override fun create(name: GameName): Either<List<String>, GameResponse> {
    val req = Request(Method.POST, "/api/v1/games" / name)
    val res = handler(req)

    return if (res.status == Status.CREATED) {
      Either.Right(res.extract())
    } else {
      Either.Left(res.extract<ErrorResponse>().errors)
    }
  }

  // TODO: maybe it will need a lobby later, to wait until the game is full..?
  override fun join(name: GameName): Either<Errors, ConnectionResponse> {
    val req = Request(Method.POST, "/api/v1/games" / name / "join")
    val res = handler(req)

    return if (res.status == Status.CREATED) {
      Either.Right(res.extract())
    } else {
      Either.Left(res.extract<ErrorResponse>().errors)
    }
  }
}
