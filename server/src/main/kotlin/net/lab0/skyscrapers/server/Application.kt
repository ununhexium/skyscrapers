package net.lab0.skyscrapers.server

import org.http4k.server.PolyHandler

fun served(service: Service) = PolyHandler(
  routed(service),
  socketed(service)
)
