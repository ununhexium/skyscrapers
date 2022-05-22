package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.server.value.GameName
import java.io.Writer

class NewGame(
  writer: Writer?,
  val configurer: Configurer,
  val client: SkyscraperClient
) :
  MyCliktCommand(writer, name = "new-game") {
  val name: String by option(
    "-n",
    "--name",
    help = "The name of the game"
  ).required()

  override fun run() {
    val url = configurer.loadConfiguration().server.apiUrl
    client.status(url)
      .mapLeft { myEcho(it.description) }
      .map { status ->
        client
          .create(url, GameName(name))
          .bimap(
            { errors -> errors.forEach { myEcho(it) } },
            { myEcho("Created game '$name'.") },
          )
      }
  }
}
