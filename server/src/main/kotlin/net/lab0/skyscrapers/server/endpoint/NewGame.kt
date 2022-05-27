package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.server.Service
import net.lab0.skyscrapers.api.dto.ErrorResponse
import net.lab0.skyscrapers.api.dto.GameResponse
import net.lab0.skyscrapers.server.pathGameName
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto

fun createGame(service: Service, req: Request): Response {
  val gameName = req.pathGameName()

  return if (gameName == null) {
    val errorResponse =
      ErrorResponse("The game must have a name. e.g /api/v1/games/{game_name}")

    Response(Status.BAD_REQUEST).with(
      Body
        .auto<ErrorResponse>()
        .toLens() of errorResponse
    )
  } else {
    val game = service.getGame(gameName)

    if (game != null) {
      Response(Status.BAD_REQUEST).with(
        Body.auto<ErrorResponse>().toLens() of
            ErrorResponse("The game $gameName already exists.")
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
