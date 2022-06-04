package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.lab0.skyscrapers.api.dto.AccessToken
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.clikt.struct.LastGame
import net.lab0.skyscrapers.client.http.SkyscraperClient
import java.io.Writer
import kotlin.io.path.bufferedReader
import kotlin.io.path.inputStream

class PlaceAt(writer: Writer?, val client: () -> SkyscraperClient) :
  MyCliktCommand(
    writer,
    name = "at"
  ) {
  val position by argument().convert { coordinates ->
    coordinates
      .split(",")
      .map { it.toInt() }
      .let { Position(it[0], it[1]) }
  }

  override fun run() {
    val lastGame = Json.decodeFromStream<LastGame>(
      Constants.lastJoin.inputStream()
    )

    client().place(
      GameName(lastGame.gameName),
      AccessToken(lastGame.token),
      position
    )
  }
}
