package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path

fun newGame(games: MutableMap<String, Game>, req: Request): Response {
  val gameName = req.path("name")
  val game = games[gameName]

  return if (gameName == null) Response(Status.BAD_REQUEST).body("The game must have a name. e.g /new/yourGameName.")
  else if (game != null) Response(Status.BAD_REQUEST).body("The game $gameName already exists.")
  else {
    games[gameName] = Game.new()
    Response(Status.CREATED).body("The game $gameName was created.")
  }
}