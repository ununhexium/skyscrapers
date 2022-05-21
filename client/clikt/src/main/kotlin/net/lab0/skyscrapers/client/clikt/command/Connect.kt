package net.lab0.skyscrapers.client.clikt.command

import com.github.ajalt.clikt.core.CliktCommand
import com.sksamuel.hoplite.ConfigLoader
import net.lab0.skyscrapers.client.clikt.configuration.Constants
import net.lab0.skyscrapers.client.clikt.configuration.Global
import kotlin.io.path.absolutePathString

class Connect : CliktCommand(name = "connect") {
  override fun run() {
    val config = ConfigLoader().loadConfigOrThrow<Global>(Constants.configLocation.absolutePathString())
  }
}
