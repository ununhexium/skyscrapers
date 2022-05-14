package net.lab0.skyscrapers.server

import net.lab0.skyscrapers.server.value.GameName
import org.http4k.core.Request
import org.http4k.routing.path


fun Request.pathGameName() =
  this.path("gameName")?.let(::GameName)
