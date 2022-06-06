package net.lab0.skyscrapers.server.endpoint

import arrow.core.merge
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.pathGameName
import net.lab0.skyscrapers.server.withGame
import net.lab0.skyscrapers.server.withGameName
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun createGame(service: Service, req: Request): Response =
  withGameName(req) { gameName ->
    service.getGame(gameName)
      // here this is actually the case we want -> no game exists and we can crate it.
      .mapLeft {
        val new = service.createGame(gameName)
        Response(Status.CREATED).with(
          Body.auto<GameResponse>().toLens() of
              GameResponse(gameName, new.state)
        )
      }
      .map {
        Response(Status.BAD_REQUEST).with(
          Body.auto<ErrorResponse>().toLens() of
              ErrorResponse("The game $gameName already exists.")
        )
      }.merge()
  }
