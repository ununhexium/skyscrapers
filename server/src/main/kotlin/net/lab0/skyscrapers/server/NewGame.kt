package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.server.dto.GameError
import org.http4k.core.Body
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.path

fun newGame(games: MutableMap<String, Game>, req: Request): Response {
  val gameName = req.path("name")

  return if (gameName == null) {
    val gameError =
      GameError("The game must have a name. e.g /new/yourGameName.")

    Response(Status.BAD_REQUEST).with(
      Body
        .auto<GameError>()
        .toLens() of gameError
    )
  } else {
    val game = games[gameName]

    if (game != null) {
      Response(Status.BAD_REQUEST).with(
        Body.auto<GameError>()
          .toLens() of GameError("The game $gameName already exists.")
      )
    } else {
      val new = Game.new()
      games[gameName] = new
      Response(Status.CREATED).with(
        Body.auto<GameResponse>().toLens() of GameResponse(
          gameName,
          new.state
        )
      )
    }
  }
}