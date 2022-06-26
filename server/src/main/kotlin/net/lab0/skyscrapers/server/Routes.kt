package net.lab0.skyscrapers.server

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
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.format.Jackson
import org.http4k.routing.Fallback
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.routing.webJars

// https://www.http4k.org/guide/reference/contracts/
// TODO: web UI for swagger https://stackoverflow.com/questions/61729113/how-to-expose-swagger-ui-with-http4k
// TODO https://www.http4k.org/guide/howto/deploy_webjars/

fun apiContract(service: Service) = contract {
  // TODO: completion
  renderer = OpenApi3(ApiInfo("Skyscrapers API", "v1"), Jackson)
  descriptionPath = "/swagger.json"
  routes += ShowGame(service)
  routes += NewGame(service)
  routes += JoinGame(service)
  routes += History(service)
}

private const val API_DESCRIPTION_PATH = "/api/v1/swagger.json"

fun swaggerUi(descriptionPath: String): RoutingHttpHandler = routes(
  "docs" bind GET to {
    Response(Status.FOUND).header("Location", "/docs/index.html?url=$descriptionPath")
  },
  // TODO: this version must be set by Gradle
  "/docs" bind static(Classpath("META-INF/resources/webjars/swagger-ui/3.43.0"))
)

fun routed(service: Service) = errorHandler.then(
  routes(
    "/" bind GET to { Response(OK).body("up") },

    "/api/v1/status" bind GET to GetStatus(service),

    "/api/v1/games/" bind GET to ListGames(service),

    routes("/api/v1" bind apiContract(service)),

    "/api/v1/games/{gameName}" bind GameAccessFilter(service).then(
      routes(
        "/place" bind POST to Place(service),
        "/build" bind POST to Build(service),
        "/seal" bind POST to Seal(service),
        "/win" bind POST to Win(service),
      ),
    ),

    swaggerUi(API_DESCRIPTION_PATH),
    webJars(),

    Fallback bind { req: Request -> Response(Status.NOT_FOUND).body("Not found: ${req.method} '${req.uri}'") },
  ),
)
