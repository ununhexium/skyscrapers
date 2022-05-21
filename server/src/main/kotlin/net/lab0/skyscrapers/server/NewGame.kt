package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.server.dto.GameError
import net.lab0.skyscrapers.server.dto.GameResponse
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun createGame(service: Service, req: Request): Response {
  val gameName = req.pathGameName()

  return if (gameName == null) {
    val gameError =
      GameError("The game must have a name. e.g /api/v1/games/yourGameName.")

    Response(Status.BAD_REQUEST).with(
      Body
        .auto<GameError>()
        .toLens() of gameError
    )
  } else {
    val game = service.getGame(gameName)

    if (game != null) {
      Response(Status.BAD_REQUEST).with(
        Body.auto<GameError>().toLens() of
            GameError("The game $gameName already exists.")
      )
    } else {
      val new = service.createGame(gameName)
      Response(Status.CREATED).with(
        Body.auto<GameResponse>().toLens() of
            GameResponse(gameName, new.state)
      )
    }
  }
}
