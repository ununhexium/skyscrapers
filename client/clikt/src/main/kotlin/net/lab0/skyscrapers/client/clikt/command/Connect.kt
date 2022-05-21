package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.http.SkyscraperClient
import java.io.Writer

class Connect(
  writer: Writer?,
  val configurer: Configurer,
  val skyscraperClient: SkyscraperClient,
) : MyCliktCommand(
  writer,
  name = "connect"
) {
  override fun run() {
    val config = configurer.loadConfiguration()
    val url = "http://${config.server.host}:${config.server.port}/api/"

    val result = skyscraperClient.connect(url)
    result.bimap(
      leftOperation = {
        myEcho(
          "Failed to connect to the server: $it",
          err = true
        )
      },
      rightOperation = {
        myEcho("The server has the following games:")
        it.listGames().forEach {
          myEcho(it.value)
        }
      }
    )
  }
}
