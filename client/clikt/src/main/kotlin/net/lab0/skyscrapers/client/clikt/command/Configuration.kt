package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.lab0.skyscrapers.client.clikt.MyCliktCommand
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.Writer

class Configuration(writer: Writer?) :
  MyCliktCommand(writer, name = "config"),
KoinComponent {
  private val reset by option(
    "--reset",
    help = "Reset the configuration to the default values"
  ).flag()

  private val json = Json {
    prettyPrint = true
  }

  private val configurer by inject<Configurer>()

  override fun run() {
    if (reset) {
      configurer.resetConfiguration()
      myEcho("Configuration reset.")
    } else {
      val config = json.encodeToString(configurer.loadConfiguration())
      myEcho(config)
    }
  }
}
