package net.lab0.skyscrapers.client.clikt.configuration

import java.nio.file.Paths

object Constants {
  val root = Paths.get(System.getProperty("user.home"), ".config", "skyscrapers", )
  val configLocation = root.resolve("global.json")
  val lastJoin = root.resolve("last.json")
}
