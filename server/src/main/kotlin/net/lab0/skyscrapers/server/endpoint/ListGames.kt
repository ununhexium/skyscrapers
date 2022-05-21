package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.dto.ListGamesResponse
import org.http4k.core.Body
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun listGames(service: Service): Response {
  val names = service.getGameNames()

  return Response(OK).with(
    Body.auto<ListGamesResponse>().toLens() of ListGamesResponse(names.map { it.value })
  )
}
