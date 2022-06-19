package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.server.endpoint.build
import net.lab0.skyscrapers.server.endpoint.createGame
import net.lab0.skyscrapers.server.endpoint.joinGame
import net.lab0.skyscrapers.server.endpoint.listGames
import net.lab0.skyscrapers.server.endpoint.place
import net.lab0.skyscrapers.server.endpoint.seal
import net.lab0.skyscrapers.server.endpoint.showGame
import net.lab0.skyscrapers.server.endpoint.status
import net.lab0.skyscrapers.server.endpoint.win
import net.lab0.skyscrapers.server.filter.GameAccessFilter
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.routing.Fallback
import org.http4k.routing.bind
import org.http4k.routing.routes


fun routed(service: Service) = errorHandler.then(
  routes(
    "/" bind GET to { Response(Status.OK).body("up") },

    "/api/v1/status" bind GET to { status(service) },

    "/api/v1/games/" bind GET to { listGames(service) },
    "/api/v1/games/{gameName}" bind GET to { showGame(service, it) },
    "/api/v1/games/{gameName}" bind POST to { createGame(service, it) },
    "/api/v1/games/{gameName}/join" bind POST to { joinGame(service, it) },
    "/api/v1/games/{gameName}" bind GameAccessFilter(service).then(
      routes(
        "/place" bind POST to { place(service, it) },
        "/build" bind POST to { build(service, it) },
        "/seal" bind POST to { seal(service, it) },
        "/win" bind POST to { win(service, it) },
      ),
    ),

    Fallback bind { req: Request -> Response(Status.NOT_FOUND).body("Not found: ${req.method} '${req.uri}'") },
  ),
)
