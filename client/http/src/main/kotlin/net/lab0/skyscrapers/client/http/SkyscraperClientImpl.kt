package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.server.dto.ConnectionResponse
import net.lab0.skyscrapers.server.dto.ErrorResponse
import net.lab0.skyscrapers.server.dto.GameResponse
import net.lab0.skyscrapers.server.dto.ListGamesResponse
import net.lab0.skyscrapers.server.dto.StatusResponse
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Uri
import org.http4k.format.KotlinxSerialization.auto

class SkyscraperClientImpl(val handler: HttpHandler) : SkyscraperClient {
  override fun status(url: String): Either<Status, StatusResponse> {
    val apiUrl = Uri.of(url)

    val req = Request(Method.GET, apiUrl / "status")
    val res = handler(req)
    return if (res.status == Status.OK) {
      Either.Right(res.extract())
    } else {
      Either.Left(res.status)
    }
  }

  override fun state(
    apiUrl: String,
    name: GameName
  ): Either<Errors, GameState> {
    val req = Request(Method.GET, Uri.of(apiUrl) / "games" / name)
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

  override fun listGames(apiUrl: String): List<GameName> {
    val req = Request(Method.GET, Uri.of(apiUrl) / "games")
    val res = handler(req)

    val list = Body.auto<ListGamesResponse>().toLens()(res)
    return list.names.map { GameName(it) }
  }

  override fun create(apiUrl:String, name: GameName): Either<List<String>, GameResponse> {
    val req = Request(Method.POST, Uri.of(apiUrl) / "games" / name)
    val res = handler(req)

    return if (res.status == Status.CREATED) {
      Either.Right(res.extract())
    } else {
      Either.Left(res.extract<ErrorResponse>().errors)
    }
  }

  // TODO: maybe it will need a lobby later, to wait until the game is full..?
  override fun join(apiUrl:String, name: GameName): Either<Errors, ConnectionResponse> {
    val req = Request(Method.POST, Uri.of(apiUrl) / "games" / name / "join")
    val res = handler(req)

    return if (res.status == Status.CREATED) {
      Either.Right(res.extract())
    } else {
      Either.Left(res.extract<ErrorResponse>().errors)
    }
  }
}
