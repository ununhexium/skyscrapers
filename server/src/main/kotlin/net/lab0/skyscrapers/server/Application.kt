package net.lab0.skyscrapers.server

import mu.KotlinLogging
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun served(service: Service) = PolyHandler(
  routed(service),
  socketed(service)
)

val log = KotlinLogging.logger("server-main")

fun main() {
  val port = 9000
  log.info { "Starting server on port $port..." }
  log.info { "http://localhost:$port/api/v1/swagger.json" }
  served(ServiceImpl.new()).asServer(Undertow(port)).start()
}
