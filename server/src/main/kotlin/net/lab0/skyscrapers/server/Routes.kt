package net.lab0.skyscrapers.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.routing.Fallback
import org.http4k.routing.bind
import org.http4k.routing.routes


fun routed(service: Service) = ServerFilters.CatchAll {
  when (it) {
    else -> Response(Status.INTERNAL_SERVER_ERROR).body(
      it::class.toString() + (it.message ?: "No error message")
    )
  }
}.then(
  routes(
    "/" bind GET to { Response(Status.OK).body("up") },
    "/api/v1/games/{gameName}" bind GET to { req -> showGame(service, req) },
    "/api/v1/games/{gameName}" bind POST to { req -> createGame(service, req) },
    "/api/v1/games/{gameName}/connect" bind POST to { connectToGame(service, it) },
    "/api/v1/games/{gameName}/play" bind PUT to { req -> playGame(service, req) },
    Fallback bind { req: Request -> Response(Status.NOT_FOUND).body("Not found: ${req.method} ${req.uri}") },
  )
)
