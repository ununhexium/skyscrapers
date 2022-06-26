package net.lab0.skyscrapers.server.endpoint

import net.lab0.skyscrapers.api.dto.value.GameName
import org.http4k.lens.Path

object Common {
  val gameNamePath = Path.map(::GameName).of("gameName")
}