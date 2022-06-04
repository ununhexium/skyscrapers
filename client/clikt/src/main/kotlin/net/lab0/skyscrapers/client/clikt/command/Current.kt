package net.lab0.skyscrapers.client.clikt.command

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.clikt.struct.LastGame
import java.io.Writer
import kotlin.io.path.inputStream

class Current(
  writer: Writer?,
) : MyCliktCommand(
  writer,
  name = "current"
) {
  override fun run() {
    val lastGame =
      Json.decodeFromStream<LastGame>(Constants.lastJoin.inputStream())

    myEcho(
      "The current game is ${lastGame.gameName} and the token for it is ${lastGame.token}"
    )
  }
}
