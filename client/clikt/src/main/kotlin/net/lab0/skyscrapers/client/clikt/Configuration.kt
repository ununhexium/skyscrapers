package net.lab0.skyscrapers.client.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import java.io.Writer

class Configuration(writer: Writer, val configurer: Configurer) :
  MyCliktCommand(writer, name = "config") {
  val reset by option(
    "--reset",
    help = "Reset the configuration to the default values"
  ).flag()

  val JSON = Json {
    prettyPrint = true
  }

  override fun run() {
    if (reset) {
      configurer.resetConfiguration()
      myEcho("Configuration reset.")
      echo("")
    } else {
      val config = JSON.encodeToString(configurer.loadConfiguration())
      writer.write(config)
    }
  }
}
