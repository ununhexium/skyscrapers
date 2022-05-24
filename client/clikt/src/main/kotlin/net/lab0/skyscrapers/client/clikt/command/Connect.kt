package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.http.SkyscraperClient
import java.io.Writer

class Connect(
  writer: Writer?,
  val sky: () -> SkyscraperClient,
) : MyCliktCommand(
  writer,
  name = "connect"
) {
  override fun run() {
    val result = sky().status()
    result.bimap(
      leftOperation = {
        myEcho(
          "Failed to connect to the server: $it",
          err = true
        )
      },
      rightOperation = {
        myEcho("Connected.")
      }
    )
  }
}
