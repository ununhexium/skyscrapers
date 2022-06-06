package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.SealTurnDTO
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.playTurn
import net.lab0.skyscrapers.server.toModel
import net.lab0.skyscrapers.server.withGame
import net.lab0.skyscrapers.server.withToken
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.format.KotlinxSerialization.auto

fun seal(service: Service, req: Request): Response =
  withGame(req, service) { (gameName, game) ->
    withToken(req) { accessToken ->
      val turn = Body
        .auto<SealTurnDTO>()
        .toLens()
        .extract(req)
        .toModel(gameName, accessToken, service)

      playTurn(turn!!, game)

    }
  }
