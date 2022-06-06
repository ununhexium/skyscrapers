package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.PlaceTurnDTO
import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.server.playTurn
import net.lab0.skyscrapers.server.toModel
import net.lab0.skyscrapers.server.withGame
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.format.KotlinxSerialization.auto

fun place(service: Service, req: Request): Response =
  withGame(req, service) { (gameName, game) ->
    val turn = Body
      .auto<PlaceTurnDTO>()
      .toLens()
      .extract(req)
      .toModel(gameName, service)

    playTurn(turn!!, game)
  }
