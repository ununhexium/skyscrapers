package net.lab0.skyscrapers.client.clikt.configuration

import java.nio.file.Paths

object Constants {
  val configLocation = Paths.get(System.getProperty("user.home"), ".config", "skyscrapers", "global.json")
}
