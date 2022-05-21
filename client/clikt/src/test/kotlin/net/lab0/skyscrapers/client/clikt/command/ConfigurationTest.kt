package net.lab0.skyscrapers.client.clikt.command

import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.lab0.skyscrapers.client.clikt.GameCli
import net.lab0.skyscrapers.client.clikt.configuration.Configurer
import net.lab0.skyscrapers.client.clikt.configuration.DefaultConfig
import net.lab0.skyscrapers.client.clikt.parse
import org.junit.jupiter.api.Test
import java.io.StringWriter

internal class ConfigurationTest {

  @Test
  fun `can read the configuration`() {
    val configurer = mockk<Configurer>()
    every { configurer.loadConfiguration() } returns DefaultConfig
    val output = StringWriter()

    val cli = GameCli.new(output, configurer = configurer)
    cli.parse("config")
    output.toString() shouldContain "port"
    output.toString() shouldContain "host"

    verify {
      configurer.loadConfiguration()
    }
  }

  @Test
  fun `can reset the configuration`() {
    val configurer = mockk<Configurer>()
    every { configurer.resetConfiguration() } returns Unit
    val output = StringWriter()

    val cli = GameCli.new(output, configurer)
    cli.parse("config", "--reset")
    output.toString() shouldContain "Configuration reset."

    verify {
      configurer.resetConfiguration()
    }
  }
}
