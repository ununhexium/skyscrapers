package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.http.SkyscraperClient
import java.io.Writer

class PlaceAt(writer: Writer?, val client: () -> SkyscraperClient) : MyCliktCommand(
  writer,
  name = "at"
) {
  val game by requireObject<GameName>()
  val position by argument().convert { coordinates ->
    coordinates
      .split(",")
      .map { it.toInt() }
      .let { Position(it[0], it[1]) }
  }

  override fun run() {
    // TODO: find how to auth the player
    client().place(game, 0, position)
  }
}
