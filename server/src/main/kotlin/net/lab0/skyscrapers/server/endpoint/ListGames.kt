package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.ListGamesResponse
import net.lab0.skyscrapers.server.Service
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

class ListGames(val service: Service): HttpHandler {
  override fun invoke(req: Request) =
    Response(OK).with(
      Body.auto<ListGamesResponse>().toLens() of ListGamesResponse(
        service
          .getGameNames()
          .map { it.value })
    )
}
