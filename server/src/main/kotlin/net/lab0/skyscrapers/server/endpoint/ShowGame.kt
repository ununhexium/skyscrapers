package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.withGame
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun showGame(service: Service, req: Request): Response =
  withGame(req, service) { (gameName, game) ->
    Response(OK).with(
      Body.auto<GameResponse>().toLens() of GameResponse(gameName, game.state)
    )
  }
