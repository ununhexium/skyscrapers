package net.lab0.skyscrapers.server

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status

fun playGame(service:Service, req: Request): Response {
  return Response(Status.OK)
}