package net.lab0.skyscrapers.client.http

import net.lab0.skyscrapers.server.dto.ListGamesResponse
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Uri
import org.http4k.format.KotlinxSerialization.auto

class SkyscraperMenuClientImpl(val handler: HttpHandler, val apiUrl: Uri) :
  SkyscraperMenuClient {

  override fun listGames(): List<GameName> {
    val req = Request(Method.GET, apiUrl / "games")
    val res = handler(req)

    val list = Body.auto<ListGamesResponse>().toLens()(res)
    return list.names.map { GameName(it) }
  }

  override fun join(gameName: GameName): SkyscraperGameClient {
    TODO("Not yet implemented")
  }
}