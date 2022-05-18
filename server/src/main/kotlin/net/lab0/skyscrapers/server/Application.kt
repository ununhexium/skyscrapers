package net.lab0.skyscrapers.server

import org.http4k.server.Undertow
import org.http4k.server.asServer

fun main() {
  val service = ServiceImpl.new()

  routed(service).asServer(Undertow(45678)).start()
}
