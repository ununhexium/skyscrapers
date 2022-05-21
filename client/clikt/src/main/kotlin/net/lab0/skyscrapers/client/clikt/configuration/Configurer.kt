package net.lab0.skyscrapers.client.clikt.configuration

import com.sksamuel.hoplite.ConfigLoader
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.writer

class Configurer(val path: Path) {
  private val JSON = Json {
    prettyPrint = true
  }

  fun loadConfiguration(): Global {
    if (!path.toFile().exists()) {
      resetConfiguration()
    }
    return ConfigLoader().loadConfigOrThrow(path.absolutePathString())
  }

  fun resetConfiguration() {
    val defaultConfig = JSON.encodeToString(DefaultConfig)
    val parentFile = path.toFile().parentFile
    if(!parentFile.exists()) {
      parentFile.mkdirs()
    }
    path.writer().use {
      it.write(defaultConfig)
    }
  }
}
