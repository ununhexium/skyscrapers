package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.http.SkyscraperClient
import net.lab0.skyscrapers.client.http.SkyscraperClientImpl
import org.http4k.client.OkHttp
import java.io.Writer

class Connect(
  writer: Writer?,
  val configurer: Configurer
) : MyCliktCommand(
  writer,
  name = "connect"
) {
  override fun run() {
    val config = configurer.loadConfiguration()
    val url = config.server.apiUrl

    val client = SkyscraperClientImpl(OkHttp(), url)
    val result = client.status()
    result.bimap(
      leftOperation = {
        myEcho(
          "Failed to connect to the server: $it",
          err = true
        )
      },
      rightOperation = {
        myEcho("Connected to $url")
      }
    )
  }
}
