package net.lab0.skyscrapers.client.clikt.command

import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.http.SkyscraperClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer

class Connect(writer: Writer?) :
  MyCliktCommand(writer, name = "connect"),
  KoinComponent {

  private val sky by inject<SkyscraperClient>()

  override fun run() {
    val result = sky.status()
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
