package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.server.dto.showGame
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes


fun routed(games: MutableMap<String, Game>) = routes(
  "/" bind GET to { Response(Status.OK).body("up") },
  "/api/v1/games/{name}" bind POST to { req -> newGame(games, req) },
  "/api/v1/games/{name}" bind GET to { req -> showGame(games, req) },
)
