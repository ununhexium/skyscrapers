package net.lab0.skyscrapers.server

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
  val app = routes(
    "/" bind GET to { Response(OK).body("up") },
    "/new/{name}" bind GET to { req: Request -> Response(OK).body(req.path("name")!!) },
  )

  val server = app.asServer(Undertow(45678)).start()
}
