package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes


fun routed(games: MutableMap<String, Game>) = routes(
  "/" bind Method.GET to { Response(Status.OK).body("up") },
  "/new/{name}" bind Method.GET to { req -> newGame(games, req) },
)

