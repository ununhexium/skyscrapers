package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.server.dto.ErrorResponse
import net.lab0.skyscrapers.server.dto.GameResponse
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Uri

class SkyscraperGameClientImpl(
  val handler: HttpHandler,
  val apiUrl: Uri,
  val name: GameName,
) : SkyscraperGameClient {
  override fun state(): Either<Errors, GameState> {
    val req = Request(Method.GET, apiUrl / "games" / name)
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
}