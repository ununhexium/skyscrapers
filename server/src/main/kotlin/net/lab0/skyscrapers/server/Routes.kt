package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.server.endpoint.Build
import net.lab0.skyscrapers.server.endpoint.ShowGame
import net.lab0.skyscrapers.server.endpoint.ListGames
import net.lab0.skyscrapers.server.endpoint.GetStatus
import net.lab0.skyscrapers.server.endpoint.History
import net.lab0.skyscrapers.server.endpoint.JoinGame
import net.lab0.skyscrapers.server.endpoint.Place
import net.lab0.skyscrapers.server.endpoint.NewGame
import net.lab0.skyscrapers.server.endpoint.Seal
import net.lab0.skyscrapers.server.endpoint.Win
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

    "/api/v1/status" bind GET to GetStatus(service),

    "/api/v1/games/" bind GET to ListGames(service),
    "/api/v1/games/{gameName}" bind GET to ShowGame(service),
    "/api/v1/games/{gameName}" bind POST to NewGame(service),
    "/api/v1/games/{gameName}/join" bind POST to JoinGame(service),
    "/api/v1/games/{gameName}/history" bind GET to History(service),

    "/api/v1/games/{gameName}" bind GameAccessFilter(service).then(
      routes(
        "/place" bind POST to Place(service),
        "/build" bind POST to Build(service),
        "/seal" bind POST to Seal(service),
        "/win" bind POST to Win(service),
      ),
    ),

    Fallback bind { req: Request -> Response(Status.NOT_FOUND).body("Not found: ${req.method} '${req.uri}'") },
  ),
)
