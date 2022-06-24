package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.server.endpoint.Build
import net.lab0.skyscrapers.server.endpoint.GetStatus
import net.lab0.skyscrapers.server.endpoint.History
import net.lab0.skyscrapers.server.endpoint.JoinGame
import net.lab0.skyscrapers.server.endpoint.ListGames
import net.lab0.skyscrapers.server.endpoint.NewGame
import net.lab0.skyscrapers.server.endpoint.Place
import net.lab0.skyscrapers.server.endpoint.Seal
import net.lab0.skyscrapers.server.endpoint.ShowGame
import net.lab0.skyscrapers.server.endpoint.Win
import net.lab0.skyscrapers.server.filter.GameAccessFilter
import org.http4k.contract.ContractRoute
import org.http4k.contract.contract
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.format.Jackson
import org.http4k.lens.Path
import org.http4k.routing.Fallback
import org.http4k.routing.bind
import org.http4k.routing.routes

// https://www.http4k.org/guide/reference/contracts/

fun greetRoute(service: Service): ContractRoute {
  val gameNamePath = Path.map(::GameName).of("gameName")
  val spec = "/games" / gameNamePath meta {
    summary = "Get the current state of a game."
    produces += ContentType.APPLICATION_JSON
    // TODO: complete the meta info
  } bindContract GET

  fun greet(gameName: GameName): HttpHandler = ShowGame(service)

  return spec to ::greet
}

fun apiContract(service: Service) = contract {
  // TODO: completion
  renderer = OpenApi3(ApiInfo("My great API", "v1.0"), Jackson)
  descriptionPath = "/swagger.json"
  routes += greetRoute(service)
}

fun routed(service: Service) = errorHandler.then(
  routes(
    "/" bind GET to { Response(OK).body("up") },

    "/api/v1/status" bind GET to GetStatus(service),

    "/api/v1/games/" bind GET to ListGames(service),
    routes("/api/v1" bind apiContract(service)),
//    "/api/v1/games/{gameName}" bind GET to ShowGame(service),
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
