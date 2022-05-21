package net.lab0.skyscrapers.client.clikt.configuration

import io.kotest.matchers.shouldBe
import org.assertj.core.util.Files
import org.junit.jupiter.api.Test

internal class ConfigurerTest {
  @Test
  fun `create a config file if it doesn't already exist`() {
    val random = Files.newTemporaryFolder()
    val configurer = Configurer(random.toPath().resolve("conf.json"))

    val global = configurer.loadConfiguration()

    global shouldBe DefaultConfig
  }

  @Test
  fun `create parent directories`() {
    val random = Files.newTemporaryFolder()
    val configurer = Configurer(
      random
        .toPath()
        .resolve("dir1")
        .resolve("dir2")
        .resolve("conf.json")
    )

    val global = configurer.loadConfiguration()

    global shouldBe DefaultConfig
  }
}