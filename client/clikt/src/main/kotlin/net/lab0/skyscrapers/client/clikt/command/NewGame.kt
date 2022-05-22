package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import net.lab0.skyscrapers.server.value.GameName
import org.http4k.client.OkHttp
import java.io.Writer

class NewGame(
  writer: Writer?,
  val configurer: Configurer,
) :
  MyCliktCommand(writer, name = "new-game") {
  val name: String by option(
    "-n",
    "--name",
    help = "The name of the game"
  ).required()

  override fun run() {
    val url = configurer.loadConfiguration().server.apiUrl
    val client = SkyscraperClientImpl(OkHttp(), url)
    client.status()
      .mapLeft { myEcho(it.description) }
      .map { status ->
        client
          .create(GameName(name))
          .bimap(
            { errors -> errors.forEach { myEcho(it) } },
            { myEcho("Created game '$name'.") },
          )
      }
  }
}
