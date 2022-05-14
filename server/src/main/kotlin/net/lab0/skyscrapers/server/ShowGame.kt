package net.lab0.skyscrapers.server.dto

import net.lab0.skyscrapers.logic.api.Game
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.path

fun showGame(games: MutableMap<String, Game>, req: Request): Response {
  val gameName = req.path("name")

  return Response(Status.OK)
}