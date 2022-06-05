package net.lab0.skyscrapers.client.clikt.configuration

import net.lab0.skyscrapers.api.dto.value.GameName
import java.nio.file.Paths
import kotlin.io.path.exists

object Constants {
  val root =
    Paths.get(System.getProperty("user.home"), ".config", "skyscrapers")

  val configLocation = root.resolve("global.json")

  fun lastJoin(game: GameName) =
    root
      .resolve(game.value)
      .also { if (!it.exists()) it.toFile().mkdirs() }
      .resolve("last.json")
}
