package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.logic.api.Game
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
  val games = mutableMapOf<String, Game>()

  val server = routed(games).asServer(Undertow(45678)).start()
}
