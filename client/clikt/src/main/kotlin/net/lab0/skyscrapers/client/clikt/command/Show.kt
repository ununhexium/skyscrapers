package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.server.value.GameName
import java.io.Writer

class Show(writer: Writer?, val sky: () -> SkyscraperClient) :
  MyCliktCommand(writer, name = "show") {
  val game by argument(help = "The name of the game.")
    .convert { GameName(it) }

  override fun run() {
    sky().state(game)
      .mapLeft { myEcho(it.joinToString("\n")) }
      .map { myEcho(it.toString()) }
  }
}
