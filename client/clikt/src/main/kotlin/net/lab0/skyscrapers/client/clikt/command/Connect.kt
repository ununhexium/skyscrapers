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
    val url = config.server.apiUrl

    val result = skyscraperClient.status(url)
    result.bimap(
      leftOperation = {
        myEcho(
          "Failed to connect to the server: $it",
          err = true
        )
      },
      rightOperation = {
        myEcho("Connected to $url")
        val names = it.listGames()
        if (names.isEmpty()) {
          myEcho("The server has no games.")
        } else {
          myEcho("The server has the following games:")
          names.forEach {
            myEcho(it.value)
          }
        }
      }
    )
  }
}
