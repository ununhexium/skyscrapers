package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.server.dto.GameError
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun showGame(service: Service, req: Request): Response {
  val gameName = req.pathGameName()
    ?: return Response(BAD_REQUEST).with(
      Body.auto<GameError>().toLens() of
          GameError("The name of the game must be specified. i.e: /api/v1/games/gameName")
    )

  val game = service.getGame(gameName)

  return if (game == null) {
    Response(NOT_FOUND).with(
      Body.auto<GameError>().toLens() of
          GameError("No game named '$gameName'")
    )
  } else {
    Response(OK).with(
      Body.auto<GameResponse>().toLens() of GameResponse(gameName, game.state)
    )
  }
}
