package net.lab0.skyscrapers.client.http

import arrow.core.Either
import net.lab0.skyscrapers.server.dto.ErrorResponse
import net.lab0.skyscrapers.server.dto.GameResponse
import net.lab0.skyscrapers.server.dto.ListGamesResponse
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.Uri
import org.http4k.format.KotlinxSerialization.auto

class SkyscraperMenuClientImpl(
  val handler: HttpHandler,
  val apiUrl: Uri
) : SkyscraperMenuClient {

  override fun listGames(): List<GameName> {
    val req = Request(Method.GET, apiUrl / "games")
    val res = handler(req)

    val list = Body.auto<ListGamesResponse>().toLens()(res)
    return list.names.map { GameName(it) }
  }

  override fun create(name: GameName): Either<List<String>, GameResponse> {
    val req = Request(Method.POST, apiUrl / "games" / name)
    val res = handler(req)

    return if (res.status == Status.CREATED) {
      Either.Right(res.extract())
    } else {
      Either.Left(res.extract<ErrorResponse>().errors)
    }
  }

  override fun join(gameName: GameName): SkyscraperGameClient {
    TODO("Not yet implemented")
  }
}

